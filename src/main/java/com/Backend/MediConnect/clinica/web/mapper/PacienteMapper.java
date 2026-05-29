package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.PacienteResponseDTO;
import com.Backend.MediConnect.clinica.persistance.entity.HistoriaClinica;
import com.Backend.MediConnect.clinica.persistance.entity.Paciente;

public class PacienteMapper {

    public static PacienteResponseDTO toResponse(Paciente p) {
        PacienteResponseDTO dto = new PacienteResponseDTO();
        dto.setIdPaciente(p.getIdPaciente());
        dto.setDni(p.getDni());
        dto.setPrimerNombre(p.getPrimerNombre());
        dto.setSegundoNombre(p.getSegundoNombre());
        dto.setPrimerApellido(p.getPrimerApellido());
        dto.setSegundoApellido(p.getSegundoApellido());
        dto.setCorreo(p.getCorreo());
        dto.setTelefono(p.getTelefono());
        dto.setFechaNacimiento(p.getFechaNacimiento());
        dto.setUbigeo(p.getUbigeo());

        HistoriaClinica hc = p.getHistoriaClinica();
        if (hc != null) {
            dto.setCodigoHistoriaClinica(hc.getCodigoUnico());
        }

        return dto;
    }
}