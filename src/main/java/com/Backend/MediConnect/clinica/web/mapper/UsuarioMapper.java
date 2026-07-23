package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.response.RegistroPacienteResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.UsuarioResponseDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.RolUsuario;
import com.Backend.MediConnect.clinica.persistance.entity.Persona;
import com.Backend.MediConnect.clinica.persistance.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponseDTO toResponse(Usuario usuario, Persona persona) {
        return UsuarioResponseDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .correo(usuario.getCorreo())
                .idRol(usuario.getIdRol())
                .nombreRol(RolUsuario.fromId(usuario.getIdRol()).name())
                .idSede(usuario.getIdSede())
                .estado(usuario.getEstado())
                .intentosFallidos(usuario.getIntentosFallidos())
                .fechaBloqueo(usuario.getFechaBloqueo())
                .dni(persona != null ? persona.getDni() : null)
                .nombres(persona != null ? persona.getNombres() : null)
                .apellidoPaterno(persona != null ? persona.getApellidoPaterno() : null)
                .apellidoMaterno(persona != null ? persona.getApellidoMaterno() : null)
                .fechaNacimiento(persona != null && persona.getFechaNacimiento() != null
                        ? persona.getFechaNacimiento().toString() : null)
                .sexo(persona != null ? persona.getSexo() : null)
                .estadoCivil(persona != null ? persona.getEstadoCivil() : null)
                .direccion(persona != null ? persona.getDireccion() : null)
                .departamento(persona != null ? persona.getDepartamento() : null)
                .provincia(persona != null ? persona.getProvincia() : null)
                .distrito(persona != null ? persona.getDistrito() : null)
                .fotoPerfil(persona != null ? persona.getFotoPerfil() : null)
                .fechaCreacion(usuario.getFechaCreacion())
                .fechaModificacion(usuario.getFechaModificacion())
                .build();
    }

    public RegistroPacienteResponseDTO toRegistroResponse(Usuario usuario, Persona persona) {
        return RegistroPacienteResponseDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombres(persona != null ? persona.getNombres() : null)
                .apellidoPaterno(persona != null ? persona.getApellidoPaterno() : null)
                .apellidoMaterno(persona != null ? persona.getApellidoMaterno() : null)
                .build();
    }
}