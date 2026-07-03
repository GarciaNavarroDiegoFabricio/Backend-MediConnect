package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.EspecialidadResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.SedeResponseDTO;
import com.Backend.MediConnect.clinica.persistance.entity.Especialidad;
import com.Backend.MediConnect.clinica.persistance.entity.Sede;

public class MantenimientoMapper {

    public static SedeResponseDTO toSedeResponse(Sede sede) {
        SedeResponseDTO dto = new SedeResponseDTO();
        dto.setIdSede(sede.getIdSede());
        dto.setNombreSede(sede.getNombreSede());
        dto.setUbicacion(sede.getUbicacion());
        dto.setTelefono(sede.getTelefono());
        dto.setActivo(sede.getActivo());
        return dto;
    }

    public static EspecialidadResponseDTO toEspecialidadResponse(Especialidad especialidad) {
        EspecialidadResponseDTO dto = new EspecialidadResponseDTO();
        dto.setIdEspecialidad(especialidad.getIdEspecialidad());
        dto.setNombreEspecialidad(especialidad.getNombreEspecialidad());
        dto.setActivo(especialidad.getActivo());
        return dto;
    }
}