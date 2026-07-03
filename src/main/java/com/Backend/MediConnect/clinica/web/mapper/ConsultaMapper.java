package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.ConsultaResponseDTO;
import com.Backend.MediConnect.clinica.persistance.entity.Consulta;

public class ConsultaMapper {

    public static ConsultaResponseDTO toResponse(Consulta consulta) {

        ConsultaResponseDTO dto = new ConsultaResponseDTO();

        dto.setIdConsulta(consulta.getIdConsulta());

        dto.setIdCita(consulta.getCita().getIdCita());

        dto.setHoraInicio(consulta.getHoraInicio());

        dto.setHoraFin(consulta.getHoraFin());

        dto.setEstado(consulta.getEstado());

        return dto;

    }

}
