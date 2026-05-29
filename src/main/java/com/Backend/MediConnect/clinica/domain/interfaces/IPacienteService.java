package com.Backend.MediConnect.clinica.domain.interfaces;

import java.util.List;

import com.Backend.MediConnect.clinica.domain.dto.CitaDTO;
import com.Backend.MediConnect.clinica.domain.dto.CitaResponseDTO;

public interface IPacienteService {
    CitaResponseDTO generarCita(String dniPaciente, CitaDTO dto);

    List<CitaResponseDTO> consultarCitas(String dniPaciente);

    void cancelarCita(String dniPaciente, Integer idCita);
}