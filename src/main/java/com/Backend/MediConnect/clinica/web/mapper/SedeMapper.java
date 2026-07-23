package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.response.SedePublicaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.SedeResponseDTO;
import com.Backend.MediConnect.clinica.persistance.entity.Sede;
import org.springframework.stereotype.Component;

@Component
public class SedeMapper {

    public SedeResponseDTO toResponse(Sede sede) {
        return SedeResponseDTO.builder()
                .idSede(sede.getIdSede())
                .nombre(sede.getNombre())
                .descripcion(sede.getDescripcion())
                .direccion(sede.getDireccion())
                .foto(sede.getFoto())
                .estado(sede.getEstado())
                .fechaCreacion(sede.getFechaCreacion())
                .usuarioCreacion(sede.getUsuarioCreacion())
                .fechaModificacion(sede.getFechaModificacion())
                .usuarioModificacion(sede.getUsuarioModificacion())
                .build();
    }

    public SedePublicaResponseDTO toPublicaResponse(Sede sede) {
        return SedePublicaResponseDTO.builder()
                .idSede(sede.getIdSede())
                .nombre(sede.getNombre())
                .descripcion(sede.getDescripcion())
                .direccion(sede.getDireccion())
                .foto(sede.getFoto())
                .estado(sede.getEstado())
                .build();
    }
}