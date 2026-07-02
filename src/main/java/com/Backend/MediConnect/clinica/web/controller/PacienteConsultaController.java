package com.Backend.MediConnect.clinica.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Backend.MediConnect.clinica.domain.dto.PacienteResponseDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.IPacienteConsultaService;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteConsultaController {

    private final IPacienteConsultaService pacienteConsultaService;

    public PacienteConsultaController(IPacienteConsultaService pacienteConsultaService) {
        this.pacienteConsultaService = pacienteConsultaService;
    }

    @GetMapping("/{dni}")
    public ResponseEntity<PacienteResponseDTO> buscarPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(pacienteConsultaService.buscarPorDni(dni));
    }
}
