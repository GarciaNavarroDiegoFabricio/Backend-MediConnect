package com.Backend.MediConnect.controller;

import com.Backend.MediConnect.dto.PatientProfileResponse;
import com.Backend.MediConnect.entity.User;
import com.Backend.MediConnect.repository.UserRepository;
import com.Backend.MediConnect.service.ReniecService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final ReniecService reniecService;
    private final UserRepository userRepository;

    public PatientController(ReniecService reniecService, UserRepository userRepository) {
        this.reniecService = reniecService;
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public ResponseEntity<PatientProfileResponse> getProfile(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return ResponseEntity.ok(reniecService.getPatientByDni(user.getDni()));
    }
}