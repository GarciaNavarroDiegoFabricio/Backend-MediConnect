package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.HistorialClinicoAgrupadoDTO;
import com.Backend.MediConnect.clinica.domain.services.HistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class HistorialController {

    @Autowired
    private HistorialService historialService;

    // Permite al medico obtener el historial clinico de un paciente especifico
    @PreAuthorize("hasAnyRole('MEDICO','PACIENTE')")
    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialClinicoAgrupadoDTO>> historial(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                historialService.obtenerHistorial(id));
    }
}