package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.CitaResponseDTO;
import com.Backend.MediConnect.clinica.persistance.entity.Cita;

public class CitaMapper {

    public static CitaResponseDTO toResponse(Cita cita) {
        if (cita == null) return null;
        
        CitaResponseDTO dto = new CitaResponseDTO();
        dto.setIdCita(cita.getIdCita());
        dto.setFecha(cita.getFecha());
        dto.setHora(cita.getHora());
        dto.setEspecialidad(cita.getEspecialidad());
        dto.setTipo(cita.getTipo());
        dto.setPrioridad(cita.getPrioridad());
        dto.setDuracionEstimada(cita.getDuracionEstimada());
        dto.setEstado(cita.getEstado());
        
        if (cita.getMedico() != null) {
            dto.setNombreMedico(cita.getMedico().getPrimerNombre() + " " + cita.getMedico().getPrimerApellido());
            dto.setDniMedico(cita.getMedico().getDni());
        }
        if (cita.getPaciente() != null) {
            dto.setNombrePaciente(cita.getPaciente().getPrimerNombre() + " " + cita.getPaciente().getPrimerApellido());
            dto.setDniPaciente(cita.getPaciente().getDni());
        }
        if (cita.getSede() != null) {
            dto.setNombreSede(cita.getSede().getNombreSede());
        }
        return dto;
    }
}