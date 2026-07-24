package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.response.ApiResponse;
import com.Backend.MediConnect.clinica.domain.dto.response.ExpedienteClinicoResponseDTO;
import com.Backend.MediConnect.clinica.domain.services.ExpedienteClinicoService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expedientes")
@Tag(name = "Expedientes clínicos")
@SecurityRequirement(name = "bearerAuth")
public class ExpedienteClinicoController {

    private final ExpedienteClinicoService expedienteService;

    public ExpedienteClinicoController(
            ExpedienteClinicoService expedienteService) {

        this.expedienteService = expedienteService;
    }

    @GetMapping("/paciente/{idPaciente}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL','ADMINISTRADOR_LOCAL','MEDICO','PACIENTE')")
    public ResponseEntity<ApiResponse<ExpedienteClinicoResponseDTO>> consultar(
            @PathVariable Long idPaciente) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        expedienteService.consultarPorPaciente(idPaciente)));

    }

}