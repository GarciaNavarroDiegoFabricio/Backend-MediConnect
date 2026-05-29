package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.RecetaResponseDTO;
import com.Backend.MediConnect.clinica.persistance.entity.Receta;

public class RecetaMapper {

    public static RecetaResponseDTO toResponse(Receta receta) {
        if (receta == null) return null;

        RecetaResponseDTO dto = new RecetaResponseDTO();
        dto.setIdReceta(receta.getIdReceta());
        dto.setPrescripcion(receta.getPrescripcion());
        dto.setFecha(receta.getFecha());
        
        if (receta.getMedico() != null) {
            dto.setNombreMedico(receta.getMedico().getPrimerNombre() + " " + receta.getMedico().getPrimerApellido());
        }
        if (receta.getPaciente() != null) {
            dto.setNombrePaciente(receta.getPaciente().getPrimerNombre() + " " + receta.getPaciente().getPrimerApellido());
        }
        if (receta.getConsulta() != null) {
            dto.setIdConsulta(receta.getConsulta().getIdConsulta());
        }
        return dto;
    }
}