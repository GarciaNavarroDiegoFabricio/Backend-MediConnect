package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.request.*;
import com.Backend.MediConnect.clinica.domain.dto.response.*;
import com.Backend.MediConnect.clinica.domain.services.AuthService;
import com.Backend.MediConnect.clinica.domain.services.PerfilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Login, registro, reset y datos de sesión")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {

    private final AuthService authService;
    private final PerfilService perfilService;

    public AuthController(AuthService authService, PerfilService perfilService) {
        this.authService = authService;
        this.perfilService = perfilService;
    }

    @Operation(summary = "Iniciar sesión con DNI y contraseña (RF-1, RF-4)")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Inicio de sesión exitoso.", response));
    }

    @Operation(summary = "Registro directo de paciente (autocompleta con RENIEC)")
    @PostMapping("/registro-paciente")
    public ResponseEntity<ApiResponse<RegistroPacienteResponseDTO>> registroPaciente(
            @Valid @RequestBody RegistroPacienteRequestDTO request) {
        RegistroPacienteResponseDTO creado = authService.registrarPaciente(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Paciente registrado correctamente.", creado));
    }

    @Operation(summary = "Solicitar restablecimiento de contraseña por correo")
    @PostMapping("/reset-password/solicitar")
    public ResponseEntity<ApiResponse<Object>> solicitarReset(@Valid @RequestBody SolicitarResetRequestDTO request) {
        authService.solicitarReset(request);
        return ResponseEntity.ok(ApiResponse.success("Se envió un correo con las instrucciones.", null));
    }

    @Operation(summary = "Confirmar restablecimiento de contraseña con token")
    @PostMapping("/reset-password/confirmar")
    public ResponseEntity<ApiResponse<Object>> confirmarReset(@Valid @RequestBody ConfirmarResetRequestDTO request) {
        authService.confirmarReset(request);
        return ResponseEntity.ok(ApiResponse.success("Contraseña restablecida correctamente.", null));
    }

    @Operation(summary = "Renovar access token mediante refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> refresh(@Valid @RequestBody RefreshTokenRequestDTO request) {
        LoginResponseDTO response = authService.refrescarToken(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success("Token renovado correctamente.", response));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener DNI, nombre completo y rol del usuario autenticado")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MeResponseDTO>> me(Authentication authentication) {
        Long idUsuario = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(perfilService.obtenerMe(idUsuario)));
    }
}