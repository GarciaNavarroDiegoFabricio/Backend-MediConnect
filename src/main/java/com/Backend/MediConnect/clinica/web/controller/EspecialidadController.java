package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.request.EspecialidadRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.ApiResponse;
import com.Backend.MediConnect.clinica.domain.dto.response.EspecialidadPublicaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.EspecialidadResponseDTO;
import com.Backend.MediConnect.clinica.domain.services.EspecialidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
@Tag(name = "Especialidades", description = "Gestión de especialidades médicas")
@SecurityRequirement(name = "bearerAuth")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    public EspecialidadController(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Registrar una nueva especialidad")
    @PostMapping
    public ResponseEntity<ApiResponse<EspecialidadResponseDTO>> crear(
            @Valid @RequestBody EspecialidadRequestDTO request, Authentication authentication) {
        EspecialidadResponseDTO creada = especialidadService.crear(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Especialidad creada correctamente.", creada));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Actualizar una especialidad")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EspecialidadResponseDTO>> actualizar(
            @PathVariable Long id, @Valid @RequestBody EspecialidadRequestDTO request, Authentication authentication) {
        EspecialidadResponseDTO actualizada = especialidadService.actualizar(id, request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Especialidad actualizada correctamente.", actualizada));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Eliminar una especialidad")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable Long id) {
        especialidadService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Especialidad eliminada correctamente.", null));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Consultar una especialidad por ID con datos completos")
    @GetMapping("/{id}/detalle")
    public ResponseEntity<ApiResponse<EspecialidadResponseDTO>> consultarDetalle(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(especialidadService.consultarPorId(id)));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Listar todas las especialidades con datos completos")
    @GetMapping("/detalle")
    public ResponseEntity<ApiResponse<List<EspecialidadResponseDTO>>> listarDetalle() {
        return ResponseEntity.ok(ApiResponse.success(especialidadService.listar()));
    }

    @Operation(summary = "Consultar una especialidad por ID (público)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EspecialidadPublicaResponseDTO>> consultar(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(especialidadService.consultarPorIdPublico(id)));
    }

    @Operation(summary = "Listar todas las especialidades (público)")
    @GetMapping
    public ResponseEntity<ApiResponse<List<EspecialidadPublicaResponseDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.success(especialidadService.listarPublico()));
    }
}