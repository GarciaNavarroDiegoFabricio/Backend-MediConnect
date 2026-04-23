package com.Backend.MediConnect.market.domain.interfaces;

import com.Backend.MediConnect.market.domain.dto.CitaDTO;
import com.Backend.MediConnect.market.domain.dto.CitaResponseDTO;
import java.util.List;

public interface IPacienteService {
    CitaResponseDTO generarCita(String dniPaciente, CitaDTO dto);
    List<CitaResponseDTO> consultarCitas(String dniPaciente);
    void cancelarCita(String dniPaciente, Integer idCita);
}