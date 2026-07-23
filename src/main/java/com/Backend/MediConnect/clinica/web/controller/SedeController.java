package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.request.SedeRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.SedeUpdateRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.ApiResponse;
import com.Backend.MediConnect.clinica.domain.dto.response.SedePublicaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.SedeResponseDTO;
import com.Backend.MediConnect.clinica.domain.services.SedeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/sedes")
@Tag(name = "Sedes", description = "Gestión de sedes de atención")
@SecurityRequirement(name = "bearerAuth")
public class SedeController {

    private final SedeService sedeService;

    public SedeController(SedeService sedeService) {
        this.sedeService = sedeService;
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Registrar una nueva sede")
    @PostMapping
    public ResponseEntity<ApiResponse<SedeResponseDTO>> crear(
            @Valid @RequestBody SedeRequestDTO request, Authentication authentication) {
        SedeResponseDTO creada = sedeService.crear(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Sede creada correctamente.", creada));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Actualizar datos de una sede")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SedeResponseDTO>> actualizar(
            @PathVariable Long id, @Valid @RequestBody SedeUpdateRequestDTO request, Authentication authentication) {
        SedeResponseDTO actualizada = sedeService.actualizar(id, request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Sede actualizada correctamente.", actualizada));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Subir o actualizar la foto de una sede")
    @PutMapping(value = "/{id}/foto", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<String>> actualizarFoto(
            @PathVariable Long id, @RequestParam("archivo") MultipartFile archivo, Authentication authentication) {
        String url = sedeService.actualizarFoto(id, archivo, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Foto de la sede actualizada correctamente.", url));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Eliminar la foto de una sede")
    @DeleteMapping("/{id}/foto")
    public ResponseEntity<ApiResponse<Object>> eliminarFoto(@PathVariable Long id, Authentication authentication) {
        sedeService.eliminarFoto(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Foto de la sede eliminada correctamente.", null));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Inactivar una sede")
    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<ApiResponse<Object>> inactivar(@PathVariable Long id, Authentication authentication) {
        sedeService.inactivar(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Sede inactivada correctamente.", null));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Activar una sede")
    @PatchMapping("/{id}/activar")
    public ResponseEntity<ApiResponse<Object>> activar(@PathVariable Long id, Authentication authentication) {
        sedeService.activar(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Sede activada correctamente.", null));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Eliminar una sede")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable Long id) {
        sedeService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Sede eliminada correctamente.", null));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Consultar una sede por ID con datos completos")
    @GetMapping("/{id}/detalle")
    public ResponseEntity<ApiResponse<SedeResponseDTO>> consultarDetalle(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(sedeService.consultarPorId(id)));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Listar todas las sedes con datos completos")
    @GetMapping("/detalle")
    public ResponseEntity<ApiResponse<List<SedeResponseDTO>>> listarDetalle() {
        return ResponseEntity.ok(ApiResponse.success(sedeService.listar()));
    }

    @Operation(summary = "Consultar una sede por ID (público)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SedePublicaResponseDTO>> consultar(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(sedeService.consultarPorIdPublico(id)));
    }

    @Operation(summary = "Listar todas las sedes (público)")
    @GetMapping
    public ResponseEntity<ApiResponse<List<SedePublicaResponseDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.success(sedeService.listarPublico()));
    }
}