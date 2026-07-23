package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.response.ApiResponse;
import com.Backend.MediConnect.clinica.domain.services.FotoPerfilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/foto-perfil")
@Tag(name = "Foto de Perfil", description = "Gestión de foto de perfil mediante Cloudinary")
@SecurityRequirement(name = "bearerAuth")
public class FotoPerfilController {

    private final FotoPerfilService fotoPerfilService;

    public FotoPerfilController(FotoPerfilService fotoPerfilService) {
        this.fotoPerfilService = fotoPerfilService;
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Subir o actualizar mi propia foto de perfil")
    @PutMapping(value = "/mi-foto", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<String>> actualizarMiFoto(
            Authentication authentication, @RequestParam("archivo") MultipartFile archivo) {
        Long idUsuario = (Long) authentication.getPrincipal();
        String url = fotoPerfilService.actualizarFotoPorIdUsuario(idUsuario, archivo);
        return ResponseEntity.ok(ApiResponse.success("Foto de perfil actualizada correctamente.", url));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Eliminar mi propia foto de perfil")
    @DeleteMapping("/mi-foto")
    public ResponseEntity<ApiResponse<Object>> eliminarMiFoto(Authentication authentication) {
        Long idUsuario = (Long) authentication.getPrincipal();
        fotoPerfilService.eliminarFotoPorIdUsuario(idUsuario);
        return ResponseEntity.ok(ApiResponse.success("Foto de perfil eliminada correctamente.", null));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'RECEPCIONISTA')")
    @Operation(summary = "Subir o actualizar la foto de perfil de otro usuario mediante su DNI")
    @PutMapping(value = "/{dni}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<String>> actualizarFotoPorDni(
            @PathVariable String dni, @RequestParam("archivo") MultipartFile archivo) {
        String url = fotoPerfilService.actualizarFotoPorDni(dni, archivo);
        return ResponseEntity.ok(ApiResponse.success("Foto de perfil actualizada correctamente.", url));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'RECEPCIONISTA')")
    @Operation(summary = "Eliminar la foto de perfil de otro usuario mediante su DNI")
    @DeleteMapping("/{dni}")
    public ResponseEntity<ApiResponse<Object>> eliminarFotoPorDni(@PathVariable String dni) {
        fotoPerfilService.eliminarFotoPorDni(dni);
        return ResponseEntity.ok(ApiResponse.success("Foto de perfil eliminada correctamente.", null));
    }
}