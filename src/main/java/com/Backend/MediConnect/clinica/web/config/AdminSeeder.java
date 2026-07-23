package com.Backend.MediConnect.clinica.web.config;

import com.Backend.MediConnect.clinica.domain.dto.response.ReniecResponseDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.IReniecService;
import com.Backend.MediConnect.clinica.domain.interfaces.RolUsuario;
import com.Backend.MediConnect.clinica.domain.repository.IPersonaRepository;
import com.Backend.MediConnect.clinica.domain.repository.IUsuarioRepository;
import com.Backend.MediConnect.clinica.domain.services.EmailService;
import com.Backend.MediConnect.clinica.persistance.entity.Persona;
import com.Backend.MediConnect.clinica.persistance.entity.Usuario;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AdminSeeder implements CommandLineRunner {

    private static final String DNI_ADMIN = "72317167";
    private static final String CORREO_ADMIN = "juan404999@gmail.com";
    private static final String PASSWORD_ADMIN = "123456";

    private final IUsuarioRepository usuarioRepository;
    private final IPersonaRepository personaRepository;
    private final PasswordEncoder passwordEncoder;
    private final IReniecService reniecService;
    private final EmailService emailService;

    public AdminSeeder(IUsuarioRepository usuarioRepository, IPersonaRepository personaRepository,
                       PasswordEncoder passwordEncoder, IReniecService reniecService, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
        this.passwordEncoder = passwordEncoder;
        this.reniecService = reniecService;
        this.emailService = emailService;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) return;

        Usuario admin = Usuario.builder()
                .correo(CORREO_ADMIN)
                .contrasenaHash(passwordEncoder.encode(PASSWORD_ADMIN))
                .idRol(RolUsuario.ADMINISTRADOR_TOTAL.getId())
                .idSede(null)
                .estado("ACTIVO")
                .intentosFallidos(0)
                .usuarioCreacion("SISTEMA")
                .build();

        admin = usuarioRepository.save(admin);

        ReniecResponseDTO datos = reniecService.consultarDni(DNI_ADMIN);

        Persona.PersonaBuilder builder = Persona.builder()
                .usuario(admin)
                .dni(DNI_ADMIN);

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
                safe(persona.getNombres()), safe(persona.getApellidoPaterno()), safe(persona.getApellidoMaterno())).trim();

        RolUsuario rol = RolUsuario.ADMINISTRADOR_TOTAL;

        emailService.enviarCorreoBienvenida(
                admin.getCorreo(),
                nombreCompleto.isBlank() ? "Administrador Total" : nombreCompleto,
                rol.getNombre(),
                rol.getDescripcionFuncionalidades()
        );
    }

    private LocalDate parsearFecha(String fecha) {
        try {
            return (fecha == null || fecha.isBlank()) ? null : LocalDate.parse(fecha);
        } catch (Exception e) {
            return null;
        }
    }

    private String safe(String valor) {
        return valor != null ? valor : "";
    }
}