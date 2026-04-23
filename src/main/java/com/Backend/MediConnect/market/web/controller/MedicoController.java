package com.Backend.MediConnect.market.web.controller;

import com.Backend.MediConnect.market.domain.dto.CitaResponseDTO;
import com.Backend.MediConnect.market.domain.dto.RecetaDTO;
import com.Backend.MediConnect.market.domain.dto.RecetaResponseDTO;
import com.Backend.MediConnect.market.domain.dto.ReporteResponseDTO;
import com.Backend.MediConnect.market.domain.interfaces.IMedicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medico")
public class MedicoController {

    private final IMedicoService medicoService;

    public MedicoController(IMedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PutMapping("/disponibilidad")
    public ResponseEntity<Void> cambiarDisponibilidad(@RequestParam Boolean disponible, Authentication auth) {
        medicoService.cambiarDisponibilidad(auth.getName(), disponible);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reporte")
    public ResponseEntity<ReporteResponseDTO> generarReporte(Authentication auth) {
        return ResponseEntity.ok(medicoService.generarReporteConsulta(auth.getName()));
    }

    @PostMapping("/receta")
    public ResponseEntity<RecetaResponseDTO> crearReceta(@RequestBody RecetaDTO dto, Authentication auth) {
        return ResponseEntity.ok(medicoService.crearReceta(auth.getName(), dto));
    }

    @GetMapping("/reservas")
    public ResponseEntity<List<CitaResponseDTO>> consultarReservas(Authentication auth) {
        return ResponseEntity.ok(medicoService.consultarReservas(auth.getName()));
    }
}