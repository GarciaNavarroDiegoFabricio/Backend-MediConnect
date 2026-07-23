package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.request.PerfilUpdateRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.ApiResponse;
import com.Backend.MediConnect.clinica.domain.dto.response.UsuarioResponseDTO;
import com.Backend.MediConnect.clinica.domain.services.PerfilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
@Tag(name = "Perfil", description = "Consulta y actualización del perfil propio")
@SecurityRequirement(name = "bearerAuth")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Ver mi perfil completo")
    @GetMapping
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> verPerfil(Authentication authentication) {
        Long idUsuario = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(perfilService.obtenerPerfil(idUsuario)));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Actualizar mi perfil")
    @PutMapping
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> actualizarPerfil(
            Authentication authentication, @Valid @RequestBody PerfilUpdateRequestDTO request) {
        Long idUsuario = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success("Perfil actualizado correctamente.",
                perfilService.actualizarPerfil(idUsuario, request)));
    }
}