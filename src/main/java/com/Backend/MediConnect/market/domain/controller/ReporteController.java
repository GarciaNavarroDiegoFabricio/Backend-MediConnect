package com.Backend.MediConnect.market.domain.controller;

import com.Backend.MediConnect.market.domain.dto.ReporteDTO;
import com.Backend.MediConnect.market.domain.services.ReporteService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reportes")

public class ReporteController {

    private final ReporteService reporteService;

    // Constructor con inyección de dependencias
    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    // Endpoint GET para generar un reporte de consultas según una fecha
    @GetMapping("/consultas")
    public ReporteDTO generarReporte(@RequestParam String fecha) {

        LocalDate fechaParsed = LocalDate.parse(fecha);
        return reporteService.generarReporteConsulta(fechaParsed);
    }
}
