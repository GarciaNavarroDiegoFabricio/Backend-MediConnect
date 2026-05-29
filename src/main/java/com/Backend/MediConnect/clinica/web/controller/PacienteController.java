package com.Backend.MediConnect.clinica.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.Backend.MediConnect.clinica.domain.dto.CitaDTO;
import com.Backend.MediConnect.clinica.domain.dto.CitaResponseDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.IPacienteService;

import java.util.List;

@RestController
@RequestMapping("/api/paciente")
public class PacienteController {

    private final IPacienteService pacienteService;

    public PacienteController(IPacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping("/cita")
    public ResponseEntity<CitaResponseDTO> generarCita(@RequestBody CitaDTO dto, Authentication auth) {
        return ResponseEntity.ok(pacienteService.generarCita(auth.getName(), dto));
    }

    @GetMapping("/citas")
    public ResponseEntity<List<CitaResponseDTO>> consultarCitas(Authentication auth) {
        return ResponseEntity.ok(pacienteService.consultarCitas(auth.getName()));
    }

    @PutMapping("/cita/{idCita}/cancelar")
    public ResponseEntity<Void> cancelarCita(@PathVariable Integer idCita, Authentication auth) {
        pacienteService.cancelarCita(auth.getName(), idCita);
        return ResponseEntity.noContent().build();
    }
}