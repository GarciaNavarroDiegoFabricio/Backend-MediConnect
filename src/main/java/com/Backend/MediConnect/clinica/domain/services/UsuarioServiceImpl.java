package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.request.UsuarioRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.UsuarioUpdateRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.ReniecResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.UsuarioResponseDTO;
import com.Backend.MediConnect.clinica.domain.exception.BusinessException;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.interfaces.IReniecService;
import com.Backend.MediConnect.clinica.domain.interfaces.IUsuarioService;
import com.Backend.MediConnect.clinica.domain.interfaces.RolUsuario;
import com.Backend.MediConnect.clinica.domain.repository.IPersonaRepository;
import com.Backend.MediConnect.clinica.domain.repository.ISedeRepository;
import com.Backend.MediConnect.clinica.domain.repository.IUsuarioRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Persona;
import com.Backend.MediConnect.clinica.persistance.entity.Usuario;
import com.Backend.MediConnect.clinica.web.mapper.UsuarioMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    private static final Long ID_ADMIN_TOTAL_INICIAL = 1L;

    private final IUsuarioRepository usuarioRepository;
    private final IPersonaRepository personaRepository;
    private final ISedeRepository sedeRepository;
    private final IReniecService reniecService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final EmailService emailService;

    public UsuarioServiceImpl(IUsuarioRepository usuarioRepository,
                              IPersonaRepository personaRepository,
                              ISedeRepository sedeRepository,
                              IReniecService reniecService,
                              PasswordEncoder passwordEncoder,
                              UsuarioMapper usuarioMapper,
                              EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
        this.sedeRepository = sedeRepository;
        this.reniecService = reniecService;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request, String usuarioCreacion) {

        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new BusinessException("El correo ya está registrado.");
        }
        if (personaRepository.existsByDni(request.getDni())) {
            throw new BusinessException("El DNI ya está registrado a otro usuario.");
        }

        RolUsuario rol = RolUsuario.fromId(request.getIdRol());

        Long idSedeFinal = null;

        if (rol.requiereSede()) {
            if (request.getIdSede() == null) {
                throw new BusinessException("La sede es obligatoria para el rol " + rol.getNombre() + ".");
            }
            sedeRepository.findById(request.getIdSede())
                    .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada."));
            idSedeFinal = request.getIdSede();
        }

        Usuario usuario = Usuario.builder()
                .correo(request.getCorreo())
                .contrasenaHash(passwordEncoder.encode(request.getContrasena()))
                .idRol(rol.getId())
                .idSede(idSedeFinal)
                .estado("ACTIVO")
                .intentosFallidos(0)
                .usuarioCreacion(usuarioCreacion)
                .build();

        usuario = usuarioRepository.save(usuario);

        Persona persona = construirPersonaDesdeReniec(request.getDni(), usuario);
        persona = personaRepository.save(persona);

        String nombreCompleto = String.join(" ",
                safe(persona.getNombres()), safe(persona.getApellidoPaterno()), safe(persona.getApellidoMaterno())).trim();

        String mensajeExtra = (rol == RolUsuario.MEDICO || rol == RolUsuario.PACIENTE)
                ? " Complete los datos adicionales según el rol para finalizar el registro."
                : "";

        emailService.enviarCorreoBienvenida(
                usuario.getCorreo(),
                nombreCompleto.isBlank() ? "Usuario MediConnect" : nombreCompleto,
                rol.getNombre(),
                rol.getDescripcionFuncionalidades()
        );

        return usuarioMapper.toResponse(usuario, persona);
    }

    private Persona construirPersonaDesdeReniec(String dni, Usuario usuario) {
        ReniecResponseDTO datos = reniecService.consultarDni(dni);

        Persona.PersonaBuilder builder = Persona.builder()
                .usuario(usuario)
                .dni(dni);

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

        return builder.build();
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

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarUsuario(Long idUsuario, UsuarioUpdateRequestDTO request, String usuarioModificacion) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        Persona persona = personaRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Datos personales no encontrados."));

        if (request.getCorreo() != null) usuario.setCorreo(request.getCorreo());
        if (request.getIdSede() != null) usuario.setIdSede(request.getIdSede());
        usuario.setUsuarioModificacion(usuarioModificacion);

        if (request.getNombres() != null) persona.setNombres(request.getNombres());
        if (request.getApellidoPaterno() != null) persona.setApellidoPaterno(request.getApellidoPaterno());
        if (request.getApellidoMaterno() != null) persona.setApellidoMaterno(request.getApellidoMaterno());
        if (request.getFechaNacimiento() != null) persona.setFechaNacimiento(parsearFecha(request.getFechaNacimiento()));
        if (request.getSexo() != null) persona.setSexo(request.getSexo());
        if (request.getEstadoCivil() != null) persona.setEstadoCivil(request.getEstadoCivil());
        if (request.getDireccion() != null) persona.setDireccion(request.getDireccion());
        if (request.getUbigeo() != null) persona.setUbigeo(request.getUbigeo());
        if (request.getDepartamento() != null) persona.setDepartamento(request.getDepartamento());
        if (request.getProvincia() != null) persona.setProvincia(request.getProvincia());
        if (request.getDistrito() != null) persona.setDistrito(request.getDistrito());

        usuario = usuarioRepository.save(usuario);
        persona = personaRepository.save(persona);

        return usuarioMapper.toResponse(usuario, persona);
    }

    @Override
    public UsuarioResponseDTO consultarPorId(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));
        Persona persona = personaRepository.findByUsuario_IdUsuario(idUsuario).orElse(null);
        return usuarioMapper.toResponse(usuario, persona);
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> usuarioMapper.toResponse(usuario,
                        personaRepository.findByUsuario_IdUsuario(usuario.getIdUsuario()).orElse(null)))
                .toList();
    }

    @Override
    @Transactional
    public void bloquearUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        usuario.setEstado("BLOQUEADO");
        usuario.setFechaBloqueo(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void inactivarUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        if (idUsuario.equals(ID_ADMIN_TOTAL_INICIAL)) {
            throw new BusinessException("No se puede inactivar al Administrador Total inicial del sistema.");
        }

        usuario.setEstado("INACTIVO");
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        validarNoEsAdminInicial(idUsuario, "eliminar");

        personaRepository.findByUsuario_IdUsuario(idUsuario).ifPresent(personaRepository::delete);
        usuarioRepository.delete(usuario);
    }

    private void validarNoEsAdminInicial(Long idUsuario, String accion) {
        if (idUsuario.equals(ID_ADMIN_TOTAL_INICIAL)) {
            throw new BusinessException("No se puede " + accion + " al Administrador Total inicial del sistema.");
        }
    }
}