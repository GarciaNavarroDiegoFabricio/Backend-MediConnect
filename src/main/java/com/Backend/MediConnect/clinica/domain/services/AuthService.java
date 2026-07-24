package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.request.ConfirmarResetRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.LoginRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.RegistroPacienteRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.SolicitarResetRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.LoginResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.RegistroPacienteResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.ReniecResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.UsuarioResponseDTO;
import com.Backend.MediConnect.clinica.domain.exception.BusinessException;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.interfaces.IReniecService;
import com.Backend.MediConnect.clinica.domain.interfaces.RolUsuario;
import com.Backend.MediConnect.clinica.domain.repository.IPersonaRepository;
import com.Backend.MediConnect.clinica.domain.repository.IUsuarioRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Persona;
import com.Backend.MediConnect.clinica.persistance.entity.Usuario;
import com.Backend.MediConnect.clinica.web.mapper.UsuarioMapper;
import com.Backend.MediConnect.clinica.web.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final IUsuarioRepository usuarioRepository;
    private final IPersonaRepository personaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final IReniecService reniecService;
    private final EmailService emailService;
    private final UsuarioMapper usuarioMapper;
    private final IntentoFallidoService intentoFallidoService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public AuthService(IUsuarioRepository usuarioRepository, IPersonaRepository personaRepository,
            PasswordEncoder passwordEncoder, JwtService jwtService, IReniecService reniecService,
            EmailService emailService, UsuarioMapper usuarioMapper,
            IntentoFallidoService intentoFallidoService) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.reniecService = reniecService;
        this.emailService = emailService;
        this.usuarioMapper = usuarioMapper;
        this.intentoFallidoService = intentoFallidoService;
    }

    @Transactional
    public LoginResponseDTO login(LoginRequestDTO request) {

        System.out.println("========== INICIO LOGIN ==========");
        System.out.println("DNI recibido: " + request.getDni());
        System.out.println("Contraseña recibida: " + request.getContrasena());

        Persona persona = personaRepository.findByDni(request.getDni())
                .orElseThrow(() -> {
                    System.out.println("ERROR: DNI no encontrado");
                    return new ResourceNotFoundException("Credenciales incorrectas.");
                });

        System.out.println("Persona encontrada:");
        System.out.println("ID Persona: " + persona.getIdPersona());
        System.out.println("Nombre: " + persona.getNombres());
        System.out.println("Apellido paterno: " + persona.getApellidoPaterno());

        Usuario usuario = persona.getUsuario();

        if (usuario == null) {
            System.out.println("ERROR: La persona no tiene usuario asociado");
            throw new BusinessException("Credenciales incorrectas.");
        }

        System.out.println("Usuario encontrado:");
        System.out.println("ID Usuario: " + usuario.getIdUsuario());
        System.out.println("Rol: " + usuario.getIdRol());
        System.out.println("Estado: " + usuario.getEstado());
        System.out.println("Hash guardado: " + usuario.getContrasenaHash());

        if ("BLOQUEADO".equals(usuario.getEstado())) {
            System.out.println("Usuario bloqueado");

            throw new BusinessException(
                    "Su cuenta está bloqueada. Revise su correo para restablecer la contraseña.");
        }

        if ("INACTIVO".equals(usuario.getEstado())) {
            System.out.println("Usuario inactivo");

            throw new BusinessException(
                    "Su cuenta está inactiva. Contacte al administrador.");
        }

        boolean passwordCorrecta = passwordEncoder.matches(
                request.getContrasena(),
                usuario.getContrasenaHash());

        System.out.println("¿Contraseña correcta?: " + passwordCorrecta);

        if (!passwordCorrecta) {

            System.out.println("ERROR: Contraseña incorrecta");

            intentoFallidoService.registrarIntentoFallido(
                    usuario.getIdUsuario(),
                    persona.getNombres(),
                    persona.getApellidoPaterno(),
                    persona.getApellidoMaterno());

            throw new BusinessException("Credenciales incorrectas.");
        }

        System.out.println("Login exitoso");

        usuario.setIntentosFallidos(0);
        usuarioRepository.save(usuario);

        String accessToken = jwtService.generarAccessToken(
                usuario.getIdUsuario(),
                usuario.getIdRol());

        String refreshToken = jwtService.generarRefreshToken(
                usuario.getIdUsuario(),
                usuario.getIdRol());

        System.out.println("Token generado correctamente");
        System.out.println("========== FIN LOGIN ==========");

        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String safe(String valor) {
        return valor != null ? valor : "";
    }

    @Transactional
    public void solicitarReset(SolicitarResetRequestDTO request) {
        Persona persona = personaRepository.findByDni(request.getDni())
                .orElseThrow(() -> new ResourceNotFoundException("DNI no registrado."));

        Usuario usuario = persona.getUsuario();

        String token = UUID.randomUUID().toString();
        usuario.setTokenReset(token);
        usuario.setTokenResetExpira(LocalDateTime.now().plusHours(1));
        usuarioRepository.save(usuario);

        String nombreCompleto = String.join(" ",
                safe(persona.getNombres()), safe(persona.getApellidoPaterno()), safe(persona.getApellidoMaterno()))
                .trim();

        String enlace = frontendUrl + "portal-web?token=" + token;
        emailService.enviarCorreoBloqueo(usuario.getCorreo(), nombreCompleto, enlace);
    }

    @Transactional
    public void confirmarReset(ConfirmarResetRequestDTO request) {
        Usuario usuario = usuarioRepository.findAll().stream()
                .filter(u -> request.getToken().equals(u.getTokenReset()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Token inválido."));

        if (usuario.getTokenResetExpira() == null || usuario.getTokenResetExpira().isBefore(LocalDateTime.now())) {
            throw new BusinessException("El token ha expirado. Solicite uno nuevo.");
        }

        usuario.setContrasenaHash(passwordEncoder.encode(request.getNuevaContrasena()));
        usuario.setEstado("ACTIVO");
        usuario.setIntentosFallidos(0);
        usuario.setFechaBloqueo(null);
        usuario.setTokenReset(null);
        usuario.setTokenResetExpira(null);

        usuarioRepository.save(usuario);
    }

    @Transactional
    public RegistroPacienteResponseDTO registrarPaciente(RegistroPacienteRequestDTO request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new BusinessException("El correo ya está registrado.");
        }
        if (personaRepository.existsByDni(request.getDni())) {
            throw new BusinessException("El DNI ya está registrado.");
        }

        Usuario usuario = Usuario.builder()
                .correo(request.getCorreo())
                .contrasenaHash(passwordEncoder.encode(request.getContrasena()))
                .idRol(RolUsuario.PACIENTE.getId())
                .idSede(null)
                .estado("ACTIVO")
                .intentosFallidos(0)
                .usuarioCreacion("PACIENTE_AUTOREGISTRO")
                .build();

        usuario = usuarioRepository.save(usuario);

        ReniecResponseDTO datos = reniecService.consultarDni(request.getDni());

        Persona.PersonaBuilder builder = Persona.builder()
                .usuario(usuario)
                .dni(request.getDni());

        if (datos.isEncontrado()) {
            builder.codigoVerificacion(datos.getCodigoVerificacion())
                    .nombres(datos.getNombres())
                    .apellidoPaterno(datos.getApellidoPaterno())
                    .apellidoMaterno(datos.getApellidoMaterno())
                    .fechaNacimiento(parsearFecha(datos.getFechaNacimiento()))
                    .sexo(datos.getSexo())
                    .estadoCivil(datos.getEstadoCivil())
                    .direccion(datos.getDireccion())
                    .ubigeo(datos.getUbigeo())
                    .departamento(datos.getDepartamento())
                    .provincia(datos.getProvincia())
                    .distrito(datos.getDistrito());
        }

        Persona persona = personaRepository.save(builder.build());

        String nombreCompleto = String.join(" ",
                safe(persona.getNombres()), safe(persona.getApellidoPaterno()), safe(persona.getApellidoMaterno()))
                .trim();

        RolUsuario rolPaciente = RolUsuario.PACIENTE;

        emailService.enviarCorreoBienvenida(
                usuario.getCorreo(),
                nombreCompleto.isBlank() ? "Paciente MediConnect" : nombreCompleto,
                rolPaciente.getNombre(),
                rolPaciente.getDescripcionFuncionalidades());

        return usuarioMapper.toRegistroResponse(usuario, persona);
    }

    private LocalDate parsearFecha(String fecha) {
        try {
            return (fecha == null || fecha.isBlank()) ? null : LocalDate.parse(fecha);
        } catch (Exception e) {
            return null;
        }
    }

    public LoginResponseDTO refrescarToken(String refreshToken) {
        if (!jwtService.esTokenValido(refreshToken) || !"REFRESH".equals(jwtService.extraerTipo(refreshToken))) {
            throw new BusinessException("Refresh token inválido o expirado.");
        }

        Long idUsuario = jwtService.extraerIdUsuario(refreshToken);
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        String nuevoAccessToken = jwtService.generarAccessToken(usuario.getIdUsuario(), usuario.getIdRol());

        return LoginResponseDTO.builder()
                .accessToken(nuevoAccessToken)
                .refreshToken(refreshToken)
                .build();
    }
}