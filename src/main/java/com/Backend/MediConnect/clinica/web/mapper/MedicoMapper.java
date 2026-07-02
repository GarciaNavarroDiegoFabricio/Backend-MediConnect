package com.Backend.MediConnect.clinica.web.mapper;

import java.util.stream.Collectors;

import com.Backend.MediConnect.clinica.domain.dto.MedicoResponseDTO;
import com.Backend.MediConnect.clinica.persistance.entity.Especialidad;
import com.Backend.MediConnect.clinica.persistance.entity.Medico;
import com.Backend.MediConnect.clinica.persistance.entity.Sede;

public class MedicoMapper {

    public static MedicoResponseDTO toResponse(Medico medico) {
        if (medico == null) return null;

        MedicoResponseDTO dto = new MedicoResponseDTO();
        dto.setIdMedico(medico.getIdMedico());
        dto.setPrimerNombre(medico.getPrimerNombre());
        dto.setSegundoNombre(medico.getSegundoNombre());
        dto.setPrimerApellido(medico.getPrimerApellido());
        dto.setSegundoApellido(medico.getSegundoApellido());
        dto.setDni(medico.getDni());
        dto.setEdad(medico.getEdad());
        dto.setDisponible(medico.getDisponible());
        
        // RF: Estado para Activar/Desactivar/Suspender
        dto.setEstado(medico.getEstado()); 

        if (medico.getEspecialidades() != null) {
            dto.setEspecialidades(medico.getEspecialidades().stream()
                    .map(Especialidad::getNombreEspecialidad)
                    .collect(Collectors.toList()));
        }

        if (medico.getSedes() != null) {
            dto.setSedes(medico.getSedes().stream()
                    .map(Sede::getNombreSede)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}