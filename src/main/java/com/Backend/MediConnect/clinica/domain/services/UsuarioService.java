package com.Backend.MediConnect.clinica.domain.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Backend.MediConnect.clinica.domain.dto.*;
import com.Backend.MediConnect.clinica.domain.interfaces.IUsuarioService;
import com.Backend.MediConnect.clinica.domain.repository.*;
import com.Backend.MediConnect.clinica.persistance.entity.*;
import com.Backend.MediConnect.clinica.web.security.JwtUtil;
import com.Backend.MediConnect.clinica.web.security.LoginAttemptService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final PacienteRepository pacienteRepo;
    private final MedicoRepository medicoRepo;
    private final AdminLocalRepository adminLocalRepo;
    private final AdminTotalRepository adminTotalRepo;
    private final SedeRepository sedeRepo;
    private final EspecialidadRepository especialidadRepo;
    private final HistoriaClinicaRepository historiaClinicaRepo;
    private final ReniecService reniecService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final HorarioRepository horarioRepo;

    public UsuarioService(UsuarioRepository usuarioRepo,
            PacienteRepository pacienteRepo,
            MedicoRepository medicoRepo,
            AdminLocalRepository adminLocalRepo,
            AdminTotalRepository adminTotalRepo,
            SedeRepository sedeRepo,
            EspecialidadRepository especialidadRepo,
            HistoriaClinicaRepository historiaClinicaRepo,
            ReniecService reniecService,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder,
            LoginAttemptService loginAttemptService,
            HorarioRepository horarioRepo) {
        this.usuarioRepo = usuarioRepo;
        this.pacienteRepo = pacienteRepo;
        this.medicoRepo = medicoRepo;
        this.adminLocalRepo = adminLocalRepo;
        this.adminTotalRepo = adminTotalRepo;
        this.sedeRepo = sedeRepo;
        this.especialidadRepo = especialidadRepo;
        this.historiaClinicaRepo = historiaClinicaRepo;
        this.reniecService = reniecService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
        this.horarioRepo = horarioRepo;
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        String dni = request.getDni();

        if (loginAttemptService.estaBloqueado(dni)) {
            throw new RuntimeException("Cuenta bloqueada temporalmente por múltiples intentos fallidos");
        }

        Usuario usuario = usuarioRepo.findByDni(dni)
                .orElseThrow(() -> {
                    loginAttemptService.registrarIntentoFallido(dni);
                    return new RuntimeException("Credenciales inválidas");
                });

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            loginAttemptService.registrarIntentoFallido(dni);
            throw new RuntimeException("Credenciales inválidas");
        }

        loginAttemptService.loginExitoso(dni);

        String nombre = resolverNombre(usuario.getDni(), usuario.getRol());
        String token = jwtUtil.generarToken(usuario.getDni(), usuario.getRol());

        return new AuthResponse(token, usuario.getRol(), nombre);
    }

    @Override
    @Transactional
    public AuthResponse registrarPaciente(RegistroPacienteDTO dto) {
        if (usuarioRepo.existsByDni(dto.getDni()))
            throw new RuntimeException("DNI ya registrado en el sistema.");

        ReniecResponseDTO reniec = reniecService.consultarDni(dto.getDni());

        if (!reniec.isEncontrado())
            throw new RuntimeException(
                    "No se encontraron datos en RENIEC para el DNI ingresado. Por favor comuníquese con un administrador para registrar sus datos manualmente.");

        Paciente paciente = new Paciente();
        paciente.setDni(dto.getDni());
        paciente.setCorreo(dto.getCorreo());
        paciente.setTelefono(dto.getTelefono());
        paciente.setPrimerNombre(reniec.getNombres());
        paciente.setPrimerApellido(reniec.getApellidoPaterno());
        paciente.setSegundoApellido(reniec.getApellidoMaterno());
        paciente.setSegundoNombre("");
        paciente.setUbigeo(reniec.getUbigeo());

        if (reniec.getFechaNacimiento() != null && !reniec.getFechaNacimiento().isEmpty())
            paciente.setFechaNacimiento(LocalDate.parse(reniec.getFechaNacimiento()));

        paciente = pacienteRepo.save(paciente);

        HistoriaClinica historia = new HistoriaClinica();
        historia.setPaciente(paciente);
        historia.setFecha(LocalDate.now());
        historia.setMotivoIngreso("Registro inicial de paciente");
        historia.setHistoriaEnfermedadActual("");
        historia.setEnfermedadesPasadas("");
        historia.setCodigoUnico(generarCodigoHistoriaClinica(paciente));
        historiaClinicaRepo.save(historia);

        return crearUsuarioYToken(dto.getDni(), dto.getPassword(), "PACIENTE",
                reniec.getNombres() + " " + reniec.getApellidoPaterno());
    }

    @Override
    @Transactional
    public AuthResponse registrarAdminLocal(RegistroAdminLocalDTO dto) {
        if (usuarioRepo.existsByDni(dto.getDni()))
            throw new RuntimeException("DNI ya registrado");

        Sede sede = sedeRepo.findById(dto.getIdSede())
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

        AdministadorLocal admin = new AdministadorLocal();
        admin.setPrimerNombre(dto.getPrimerNombre());
        admin.setSegundoNombre(dto.getSegundoNombre());
        admin.setPrimerApellido(dto.getPrimerApellido());
        admin.setSegundoApellido(dto.getSegundoApellido());
        admin.setDni(dto.getDni());
        admin.setSede(sede);
        adminLocalRepo.save(admin);

        return crearUsuarioYToken(dto.getDni(), dto.getPassword(), "ADMIN_LOCAL",
                dto.getPrimerNombre() + " " + dto.getPrimerApellido());
    }

    @Override
    @Transactional
    public AuthResponse registrarAdminTotal(RegistroAdminTotalDTO dto) {
        if (usuarioRepo.existsByDni(dto.getDni()))
            throw new RuntimeException("DNI ya registrado");

        AdministradorTotal admin = new AdministradorTotal();
        admin.setPrimerNombre(dto.getPrimerNombre());
        admin.setSegundoNombre(dto.getSegundoNombre());
        admin.setPrimerApellido(dto.getPrimerApellido());
        admin.setSegundoApellido(dto.getSegundoApellido());
        admin.setDni(dto.getDni());
        adminTotalRepo.save(admin);

        return crearUsuarioYToken(dto.getDni(), dto.getPassword(), "ADMIN_TOTAL",
                dto.getPrimerNombre() + " " + dto.getPrimerApellido());
    }

    @Override
    @Transactional
    public AuthResponse registrarMedico(RegistroMedicoDTO dto, String dniRegistrador, String rolRegistrador) {
        if (dto.getDni() == null || dto.getDni().isBlank())
            throw new RuntimeException("El DNI es obligatorio.");

        if (!dto.getDni().matches("\\d{8}"))
            throw new RuntimeException("El DNI debe tener 8 dígitos.");

        if (usuarioRepo.existsByDni(dto.getDni()))
            throw new RuntimeException("El DNI ya está registrado.");

        if (medicoRepo.existsByNumeroColegiatura(dto.getNumeroColegiatura()))
            throw new RuntimeException("La colegiatura ya existe.");

        if (dto.getEdad() == null || dto.getEdad() < 18)
            throw new RuntimeException("El médico debe ser mayor de edad.");

        if (dto.getPassword() == null || dto.getPassword().length() < 8)
            throw new RuntimeException("La contraseña debe tener mínimo 8 caracteres.");

        if (dto.getIdEspecialidades() == null || dto.getIdEspecialidades().isEmpty())
            throw new RuntimeException("Debe seleccionar al menos una especialidad.");

        Sede sede;

        if (rolRegistrador.equals("ROLE_ADMIN_LOCAL")) {

            AdministadorLocal adminLocal = adminLocalRepo.findByDni(dniRegistrador)
                    .orElseThrow(() -> new RuntimeException("Administrador Local no encontrado"));

            sede = adminLocal.getSede();

        } else {

            if (dto.getIdSede() == null)
                throw new RuntimeException("Debe seleccionar una sede.");

            sede = sedeRepo.findById(dto.getIdSede())
                    .orElseThrow(() -> new RuntimeException("La sede no existe."));
        }

        List<Especialidad> especialidades = especialidadRepo.findAllById(dto.getIdEspecialidades());

        if (especialidades.size() != dto.getIdEspecialidades().size())
            throw new RuntimeException("Una o más especialidades no existen.");

        Medico medico = new Medico();

        medico.setPrimerNombre(dto.getPrimerNombre());

        medico.setSegundoNombre(dto.getSegundoNombre());

        medico.setPrimerApellido(dto.getPrimerApellido());

        medico.setSegundoApellido(dto.getSegundoApellido());

        medico.setDni(dto.getDni());

        medico.setEdad(dto.getEdad());

        medico.setNumeroColegiatura(dto.getNumeroColegiatura());

        medico.setDisponible(
                dto.getDisponible() != null
                        ? dto.getDisponible()
                        : true);

        medico.setEstado("ACTIVO");

        medico.setEspecialidades(especialidades);

        medico.setSedes(List.of(sede));

        medico = medicoRepo.save(medico);

        if (dto.getHorarios() != null && !dto.getHorarios().isEmpty()) {

            for (HorarioDTO h : dto.getHorarios()) {

                if (h.getHoraInicio().isAfter(h.getHoraFin()))
                    throw new RuntimeException(
                            "La hora de inicio debe ser menor que la hora fin.");

                if (h.getIntervaloMinutos() <= 0)
                    throw new RuntimeException(
                            "El intervalo debe ser mayor que cero.");

                Horario horario = new Horario();

                horario.setMedico(medico);

                horario.setDiaSemana(h.getDiaSemana());

                horario.setHoraInicio(h.getHoraInicio());

                horario.setHoraFin(h.getHoraFin());

                horario.setIntervaloMinutos(h.getIntervaloMinutos());

                horario.setEstado("ACTIVO");

                horario.setMotivo(null);

                horarioRepo.save(horario);
            }

        }

        return crearUsuarioYToken(
                dto.getDni(),
                dto.getPassword(),
                "MEDICO",
                dto.getPrimerNombre() + " " + dto.getPrimerApellido());
    }

    private AuthResponse crearUsuarioYToken(String dni, String rawPassword, String rol, String nombre) {
        Usuario usuario = new Usuario();
        usuario.setDni(dni);
        usuario.setPassword(passwordEncoder.encode(rawPassword));
        usuario.setRol(rol);
        usuarioRepo.save(usuario);

        String token = jwtUtil.generarToken(dni, rol);
        return new AuthResponse(token, rol, nombre);
    }

    private String resolverNombre(String dni, String rol) {
        return switch (rol) {
            case "PACIENTE" -> pacienteRepo.findByDni(dni)
                    .map(p -> p.getPrimerNombre() + " " + p.getPrimerApellido())
                    .orElse("Paciente");
            case "MEDICO" -> medicoRepo.findByDni(dni)
                    .map(m -> m.getPrimerNombre() + " " + m.getPrimerApellido())
                    .orElse("Medico");
            case "ADMIN_LOCAL" -> adminLocalRepo.findByDni(dni)
                    .map(a -> a.getPrimerNombre() + " " + a.getPrimerApellido())
                    .orElse("Admin Local");
            case "ADMIN_TOTAL" -> adminTotalRepo.findByDni(dni)
                    .map(a -> a.getPrimerNombre() + " " + a.getPrimerApellido())
                    .orElse("Admin Total");
            default -> "Usuario";
        };
    }

    private String generarCodigoHistoriaClinica(Paciente paciente) {
        String base = "HCL-" + paciente.getDni() + "-" + LocalDateTime.now().getYear();
        String codigo = base;
        int sufijo = 1;
        while (historiaClinicaRepo.existsByCodigoUnico(codigo)) {
            codigo = base + "-" + sufijo;
            sufijo++;
        }
        return codigo;
    }

}