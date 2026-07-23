package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.response.EspecialidadPublicaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.EspecialidadResponseDTO;
import com.Backend.MediConnect.clinica.persistance.entity.Especialidad;
import org.springframework.stereotype.Component;

@Component
public class EspecialidadMapper {

    public EspecialidadResponseDTO toResponse(Especialidad especialidad) {
        return EspecialidadResponseDTO.builder()
                .idEspecialidad(especialidad.getIdEspecialidad())
                .nombre(especialidad.getNombre())
                .descripcion(especialidad.getDescripcion())
                .foto(especialidad.getFoto())
                .fechaCreacion(especialidad.getFechaCreacion())
                .usuarioCreacion(especialidad.getUsuarioCreacion())
                .fechaModificacion(especialidad.getFechaModificacion())
                .usuarioModificacion(especialidad.getUsuarioModificacion())
                .build();
    }

    public EspecialidadPublicaResponseDTO toPublicaResponse(Especialidad especialidad) {
        return EspecialidadPublicaResponseDTO.builder()
                .idEspecialidad(especialidad.getIdEspecialidad())
                .nombre(especialidad.getNombre())
                .descripcion(especialidad.getDescripcion())
                .foto(especialidad.getFoto())
                .build();
    }
}