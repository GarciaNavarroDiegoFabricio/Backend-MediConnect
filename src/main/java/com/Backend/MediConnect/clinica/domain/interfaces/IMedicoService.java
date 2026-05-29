package com.Backend.MediConnect.clinica.domain.interfaces;

import java.util.List;

import com.Backend.MediConnect.clinica.domain.dto.CitaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.RecetaDTO;
import com.Backend.MediConnect.clinica.domain.dto.RecetaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.ReporteResponseDTO;

public interface IMedicoService {
    void cambiarDisponibilidad(String dniMedico, Boolean disponible);

    ReporteResponseDTO generarReporteConsulta(String dniMedico);

    RecetaResponseDTO crearReceta(String dniMedico, RecetaDTO dto);

    List<CitaResponseDTO> consultarReservas(String dniMedico);
}