package com.Backend.MediConnect.market.domain.services;

import com.Backend.MediConnect.market.domain.dto.*;
import com.Backend.MediConnect.market.domain.interfaces.IUsuarioService;
import com.Backend.MediConnect.market.persistance.entity.*;
import com.Backend.MediConnect.market.domain.repository.*;
import com.Backend.MediConnect.market.web.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
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
    private final ReniecService reniecService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepo,
                          PacienteRepository pacienteRepo,
                          MedicoRepository medicoRepo,
                          AdminLocalRepository adminLocalRepo,
                          AdminTotalRepository adminTotalRepo,
                          SedeRepository sedeRepo,
                          EspecialidadRepository especialidadRepo,
                          ReniecService reniecService,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.pacienteRepo = pacienteRepo;
        this.medicoRepo = medicoRepo;
        this.adminLocalRepo = adminLocalRepo;
        this.adminTotalRepo = adminTotalRepo;
        this.sedeRepo = sedeRepo;
        this.especialidadRepo = especialidadRepo;
        this.reniecService = reniecService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        Usuario usuario = usuarioRepo.findByDni(request.getDni())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword()))
            throw new RuntimeException("Credenciales inválidas");

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
                    "No se encontraron datos en RENIEC para el DNI ingresado. " +
                            "Por favor comuníquese con un administrador para registrar sus datos manualmente."
            );

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

        pacienteRepo.save(paciente);

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
        if (usuarioRepo.existsByDni(dto.getDni()))
            throw new RuntimeException("DNI ya registrado");

        Sede sede;
        if (rolRegistrador.equals("ROLE_ADMIN_LOCAL")) {
            AdministadorLocal adminLocal = adminLocalRepo.findByDni(dniRegistrador)
                    .orElseThrow(() -> new RuntimeException("Admin Local no encontrado"));
            sede = adminLocal.getSede();
        } else {
            if (dto.getIdSede() == null)
                throw new RuntimeException("El Admin Total debe especificar una sede");
            sede = sedeRepo.findById(dto.getIdSede())
                    .orElseThrow(() -> new RuntimeException("Sede no encontrada"));
        }

        List<Especialidad> especialidades = especialidadRepo.findAllById(dto.getIdEspecialidades());
        if (especialidades.isEmpty())
            throw new RuntimeException("Debe asignar al menos una especialidad válida");

        Medico medico = new Medico();
        medico.setPrimerNombre(dto.getPrimerNombre());
        medico.setSegundoNombre(dto.getSegundoNombre());
        medico.setPrimerApellido(dto.getPrimerApellido());
        medico.setSegundoApellido(dto.getSegundoApellido());
        medico.setDni(dto.getDni());
        medico.setEdad(dto.getEdad());
        medico.setDisponible(true);
        medico.setEspecialidades(especialidades);
        medico.setSedes(List.of(sede));
        medicoRepo.save(medico);

        return crearUsuarioYToken(dto.getDni(), dto.getPassword(), "MEDICO",
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
}