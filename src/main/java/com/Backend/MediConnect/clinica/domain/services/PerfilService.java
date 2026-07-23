package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.request.PerfilUpdateRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.MeResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.UsuarioResponseDTO;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.interfaces.RolUsuario;
import com.Backend.MediConnect.clinica.domain.repository.IPersonaRepository;
import com.Backend.MediConnect.clinica.domain.repository.IUsuarioRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Persona;
import com.Backend.MediConnect.clinica.persistance.entity.Usuario;
import com.Backend.MediConnect.clinica.web.mapper.UsuarioMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerfilService {

    private final IUsuarioRepository usuarioRepository;
    private final IPersonaRepository personaRepository;
    private final UsuarioMapper usuarioMapper;

    public PerfilService(IUsuarioRepository usuarioRepository, IPersonaRepository personaRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
        this.usuarioMapper = usuarioMapper;
    }

    public MeResponseDTO obtenerMe(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));
        Persona persona = personaRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Datos personales no encontrados."));

        String nombreCompleto = String.join(" ",
                persona.getNombres() != null ? persona.getNombres() : "",
                persona.getApellidoPaterno() != null ? persona.getApellidoPaterno() : "",
                persona.getApellidoMaterno() != null ? persona.getApellidoMaterno() : "").trim();

        return MeResponseDTO.builder()
                .dni(persona.getDni())
                .nombreCompleto(nombreCompleto)
                .nombreRol(RolUsuario.fromId(usuario.getIdRol()).name())
                .build();
    }

    public UsuarioResponseDTO obtenerPerfil(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));
        Persona persona = personaRepository.findByUsuario_IdUsuario(idUsuario).orElse(null);
        return usuarioMapper.toResponse(usuario, persona);
    }

    @Transactional
    public UsuarioResponseDTO actualizarPerfil(Long idUsuario, PerfilUpdateRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));
        Persona persona = personaRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Datos personales no encontrados."));

        if (request.getCorreo() != null) usuario.setCorreo(request.getCorreo());
        if (request.getDireccion() != null) persona.setDireccion(request.getDireccion());
        if (request.getEstadoCivil() != null) persona.setEstadoCivil(request.getEstadoCivil());

        usuarioRepository.save(usuario);
        personaRepository.save(persona);

        return usuarioMapper.toResponse(usuario, persona);
    }
}