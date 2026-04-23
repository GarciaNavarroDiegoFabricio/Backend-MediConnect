package com.Backend.MediConnect.market.web.controller;

import com.Backend.MediConnect.market.domain.dto.ReniecResponseDTO;
import com.Backend.MediConnect.market.domain.services.ReniecService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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