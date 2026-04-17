package com.Backend.MediConnect.controller;

import com.Backend.MediConnect.dto.PatientProfileResponse;
import com.Backend.MediConnect.service.ReniecService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ReniecService reniecService;

    public AdminController(ReniecService reniecService) {
        this.reniecService = reniecService;
    }

    @GetMapping("/patient-info")
    public ResponseEntity<PatientProfileResponse> getPatientInfo(@RequestParam String dni) {
        return ResponseEntity.ok(reniecService.getPatientByDni(dni));
    }
}