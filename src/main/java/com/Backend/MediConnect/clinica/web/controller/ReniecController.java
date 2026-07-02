package com.Backend.MediConnect.clinica.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Backend.MediConnect.clinica.domain.dto.ReniecResponseDTO;
import com.Backend.MediConnect.clinica.domain.services.ReniecService;

@RestController
@RequestMapping("/api/reniec")
public class ReniecController {

    private final ReniecService reniecService;

    public ReniecController(ReniecService reniecService) {
        this.reniecService = reniecService;
    }

    @GetMapping("/consultar/{dni}")
    public ResponseEntity<ReniecResponseDTO> consultarDni(@PathVariable String dni) {
        return ResponseEntity.ok(reniecService.consultarDni(dni));
    }
}