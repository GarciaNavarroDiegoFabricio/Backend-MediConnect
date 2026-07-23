package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.request.RecetaRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.ApiResponse;
import com.Backend.MediConnect.clinica.domain.dto.response.RecetaResponseDTO;
import com.Backend.MediConnect.clinica.domain.services.RecetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
@Tag(name = "Recetas", description = "Generación y consulta de recetas médicas")
@SecurityRequirement(name = "bearerAuth")
public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    @PreAuthorize("hasRole('MEDICO')")
    @Operation(summary = "Generar una receta médica asociada a una atención")
    @PostMapping("/atenciones/{idAtencion}")
    public ResponseEntity<ApiResponse<RecetaResponseDTO>> generar(
            @PathVariable Long idAtencion, @Valid @RequestBody RecetaRequestDTO request) {
        RecetaResponseDTO receta = recetaService.generar(idAtencion, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Receta generada y enviada correctamente.", receta));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO', 'PACIENTE')")
    @Operation(summary = "Consultar la receta de una atención médica")
    @GetMapping("/atenciones/{idAtencion}")
    public ResponseEntity<ApiResponse<RecetaResponseDTO>> consultarPorAtencion(@PathVariable Long idAtencion) {
        return ResponseEntity.ok(ApiResponse.success(recetaService.consultarPorAtencion(idAtencion)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO', 'PACIENTE')")
    @Operation(summary = "Listar las recetas de un paciente")
    @GetMapping("/pacientes/{idPaciente}")
    public ResponseEntity<ApiResponse<List<RecetaResponseDTO>>> listarPorPaciente(@PathVariable Long idPaciente) {
        return ResponseEntity.ok(ApiResponse.success(recetaService.listarPorPaciente(idPaciente)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO', 'PACIENTE')")
    @Operation(summary = "Descargar el PDF de una receta")
    @GetMapping("/{idReceta}/pdf")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Long idReceta) {
        byte[] pdf = recetaService.descargarPdf(idReceta);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=receta_" + idReceta + ".pdf")
                .body(pdf);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO', 'PACIENTE')")
    @Operation(summary = "Consultar una receta por ID")
    @GetMapping("/{idReceta}")
    public ResponseEntity<ApiResponse<RecetaResponseDTO>> consultarPorId(@PathVariable Long idReceta) {
        return ResponseEntity.ok(ApiResponse.success(recetaService.consultarPorId(idReceta)));
    }
}