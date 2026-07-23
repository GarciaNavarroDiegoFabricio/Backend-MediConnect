package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.request.UsuarioRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.UsuarioUpdateRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.ApiResponse;
import com.Backend.MediConnect.clinica.domain.dto.response.UsuarioResponseDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.IUsuarioService;
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
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Creación base de Usuario + Persona para cualquier rol")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Registrar Usuario + Persona (completa datos vía RENIEC)")
    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> crear(
            @Valid @RequestBody UsuarioRequestDTO request, Authentication authentication) {
        UsuarioResponseDTO creado = usuarioService.crearUsuario(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Usuario creado correctamente.", creado));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Actualizar datos o sede de un usuario")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> actualizar(
            @PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequestDTO request, Authentication authentication) {
        UsuarioResponseDTO actualizado = usuarioService.actualizarUsuario(id, request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Usuario actualizado correctamente.", actualizado));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL')")
    @Operation(summary = "Consultar un usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> consultar(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(usuarioService.consultarPorId(id)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL')")
    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioResponseDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.success(usuarioService.listarUsuarios()));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Bloquear un usuario")
    @PatchMapping("/{id}/bloquear")
    public ResponseEntity<ApiResponse<Object>> bloquear(@PathVariable Long id) {
        usuarioService.bloquearUsuario(id);
        return ResponseEntity.ok(ApiResponse.success("Usuario bloqueado correctamente.", null));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Inactivar un usuario")
    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<ApiResponse<Object>> inactivar(@PathVariable Long id) {
        usuarioService.inactivarUsuario(id);
        return ResponseEntity.ok(ApiResponse.success("Usuario inactivado correctamente.", null));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR_TOTAL')")
    @Operation(summary = "Eliminar un usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok(ApiResponse.success("Usuario eliminado correctamente.", null));
    }
}