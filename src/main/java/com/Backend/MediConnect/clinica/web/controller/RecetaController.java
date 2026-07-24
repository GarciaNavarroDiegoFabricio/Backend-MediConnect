package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.request.RecetaRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.ApiResponse;
import com.Backend.MediConnect.clinica.domain.dto.response.RecetaResponseDTO;
import com.Backend.MediConnect.clinica.domain.services.RecetaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
@Tag(name = "Recetas")
@SecurityRequirement(name = "bearerAuth")
public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    @PreAuthorize("hasRole('MEDICO')")
    @PostMapping("/consultas/{idConsulta}")
    @Operation(summary = "Registrar receta de una consulta")
    public ResponseEntity<ApiResponse<RecetaResponseDTO>> generar(
            @PathVariable Long idConsulta,
            @Valid @RequestBody RecetaRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Receta registrada correctamente.",
                                recetaService.generar(idConsulta, request)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL','ADMINISTRADOR_LOCAL','MEDICO','PACIENTE')")
    @GetMapping("/consultas/{idConsulta}")
    @Operation(summary = "Consultar receta por consulta")
    public ResponseEntity<ApiResponse<RecetaResponseDTO>> consultarPorConsulta(
            @PathVariable Long idConsulta) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        recetaService.consultarPorConsulta(idConsulta)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL','ADMINISTRADOR_LOCAL','MEDICO','PACIENTE')")
    @GetMapping("/pacientes/{idPaciente}")
    @Operation(summary = "Listar recetas del paciente")
    public ResponseEntity<ApiResponse<List<RecetaResponseDTO>>> listarPorPaciente(
            @PathVariable Long idPaciente) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        recetaService.listarPorPaciente(idPaciente)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL','ADMINISTRADOR_LOCAL','MEDICO','PACIENTE')")
    @GetMapping("/{idReceta}/pdf")
    @Operation(summary = "Descargar receta PDF")
    public ResponseEntity<byte[]> descargarPdf(
            @PathVariable Long idReceta) {

        byte[] pdf = recetaService.descargarPdf(idReceta);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=receta_" + idReceta + ".pdf")
                .body(pdf);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL','ADMINISTRADOR_LOCAL','MEDICO','PACIENTE')")
    @GetMapping("/{idReceta}")
    @Operation(summary = "Consultar receta por ID")
    public ResponseEntity<ApiResponse<RecetaResponseDTO>> consultarPorId(
            @PathVariable Long idReceta) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        recetaService.consultarPorId(idReceta)));
    }

}