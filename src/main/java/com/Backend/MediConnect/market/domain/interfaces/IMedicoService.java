package com.Backend.MediConnect.market.domain.interfaces;

import com.Backend.MediConnect.market.domain.dto.CitaResponseDTO;
import com.Backend.MediConnect.market.domain.dto.RecetaDTO;
import com.Backend.MediConnect.market.domain.dto.RecetaResponseDTO;
import com.Backend.MediConnect.market.domain.dto.ReporteResponseDTO;
import java.util.List;

public interface IMedicoService {
    void cambiarDisponibilidad(String dniMedico, Boolean disponible);
    ReporteResponseDTO generarReporteConsulta(String dniMedico);
    RecetaResponseDTO crearReceta(String dniMedico, RecetaDTO dto);
    List<CitaResponseDTO> consultarReservas(String dniMedico);
}