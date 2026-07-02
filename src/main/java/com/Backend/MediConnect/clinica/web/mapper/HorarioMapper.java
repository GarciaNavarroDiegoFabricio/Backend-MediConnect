package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.HorarioResponseDTO;
import com.Backend.MediConnect.clinica.persistance.entity.Horario;

public class HorarioMapper {

    public static HorarioResponseDTO toResponse(Horario horario) {
        if (horario == null) return null;

        HorarioResponseDTO dto = new HorarioResponseDTO();
        dto.setIdHorario(horario.getIdHorario());
        dto.setDiaSemana(horario.getDiaSemana());
        dto.setHoraInicio(horario.getHoraInicio());
        dto.setHoraFin(horario.getHoraFin());
        dto.setIntervaloMinutos(horario.getIntervaloMinutos());
        dto.setEstado(horario.getEstado());
        
        if (horario.getMedico() != null) {
            dto.setNombreMedico(horario.getMedico().getPrimerNombre() + " " + horario.getMedico().getPrimerApellido());
            dto.setDniMedico(horario.getMedico().getDni());
        }
        return dto;
    }
}