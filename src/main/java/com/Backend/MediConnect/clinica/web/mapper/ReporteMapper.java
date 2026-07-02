package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.ReporteResponseDTO;
import com.Backend.MediConnect.clinica.persistance.entity.Reporte;

public class ReporteMapper {

    public static ReporteResponseDTO toResponse(Reporte reporte) {
        if (reporte == null) return null;
        
        ReporteResponseDTO dto = new ReporteResponseDTO();
        dto.setIdReporte(reporte.getIdReporte());
        dto.setFechaReporte(reporte.getFechaReporte());
        dto.setCitasAtendidas(reporte.getCitasAtendidas());
        dto.setCitasCanceladas(reporte.getCitasCanceladas());
        dto.setCitasReprogramadas(reporte.getCitasReprogramadas());
        dto.setCitasPendientes(reporte.getCitasPendientes());
        return dto;
    }
}