package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.request.PacienteComplementoRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.PacienteContactoUpdateDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.ApiResponse;
import com.Backend.MediConnect.clinica.domain.dto.response.PacienteResponseDTO;
import com.Backend.MediConnect.clinica.domain.services.PacienteService;
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
@RequestMapping("/api/pacientes")
@Tag(name = "Pacientes", description = "Datos clínicos y de contacto de pacientes")
@SecurityRequirement(name = "bearerAuth")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'RECEPCIONISTA')")
    @Operation(summary = "Completar datos de contacto de un paciente ya registrado en /api/usuarios")
    @PostMapping("/{idUsuario}/completar-datos")
    public ResponseEntity<ApiResponse<PacienteResponseDTO>> completarDatos(
            @PathVariable Long idUsuario, @RequestBody PacienteComplementoRequestDTO request) {
        PacienteResponseDTO completado = pacienteService.completarDatos(idUsuario, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Datos de contacto completados correctamente.", completado));
    }

    @PreAuthorize("hasRole('PACIENTE')")
    @Operation(summary = "Actualizar mis datos de contacto (restringido a datos no sensibles)")
    @PutMapping("/mi-contacto")
    public ResponseEntity<ApiResponse<PacienteResponseDTO>> actualizarMiContacto(
            @Valid @RequestBody PacienteContactoUpdateDTO request, Authentication authentication) {
        Long idUsuario = (Long) authentication.getPrincipal();
        PacienteResponseDTO actualizado = pacienteService.actualizarContacto(idUsuario, request);
        return ResponseEntity.ok(ApiResponse.success("Datos de contacto actualizados correctamente.", actualizado));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'RECEPCIONISTA')")
    @Operation(summary = "Buscar pacientes por DNI, nombres, apellidos o código de historia clínica")
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<PacienteResponseDTO>>> buscar(@RequestParam String termino) {
        return ResponseEntity.ok(ApiResponse.success(pacienteService.buscar(termino)));
    }

    @PreAuthorize("hasRole('PACIENTE')")
    @Operation(summary = "Obtener mis datos de contacto")
    @GetMapping("/mi-contacto")
    public ResponseEntity<ApiResponse<PacienteResponseDTO>> obtenerMiContacto(Authentication authentication) {

        Long idUsuario = (Long) authentication.getPrincipal();

        PacienteResponseDTO paciente = pacienteService.consultarPorIdUsuario(idUsuario);

        return ResponseEntity.ok(
                ApiResponse.success("Datos de contacto obtenidos correctamente.", paciente));
    }

}