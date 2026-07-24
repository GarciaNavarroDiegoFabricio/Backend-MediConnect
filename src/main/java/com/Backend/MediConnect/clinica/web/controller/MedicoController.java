package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.request.MedicoComplementoRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.ApiResponse;
import com.Backend.MediConnect.clinica.domain.dto.response.MedicoResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.PacienteResponseDTO;
import com.Backend.MediConnect.clinica.domain.services.MedicoService;
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
@RequestMapping("/api/medicos")
@Tag(name = "Médicos", description = "Datos profesionales, especialidad y sede")
@SecurityRequirement(name = "bearerAuth")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL')")
    @Operation(summary = "Completar datos profesionales de un médico ya registrado en /api/usuarios")
    @PostMapping("/{idUsuario}/completar-datos")
    public ResponseEntity<ApiResponse<MedicoResponseDTO>> completarDatos(
            @PathVariable Long idUsuario, @Valid @RequestBody MedicoComplementoRequestDTO request,
            Authentication authentication) {
        MedicoResponseDTO completado = medicoService.completarDatos(idUsuario, request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Datos profesionales completados correctamente.", completado));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL')")
    @Operation(summary = "Actualizar especialidad y/o sede de un médico")
    @PatchMapping("/{id}/especialidad-sede")
    public ResponseEntity<ApiResponse<MedicoResponseDTO>> actualizarEspecialidadYSede(
            @PathVariable Long id,
            @RequestParam(required = false) Long idEspecialidad,
            @RequestParam(required = false) Long idSede,
            Authentication authentication) {
        MedicoResponseDTO actualizado = medicoService.actualizarSedeYEspecialidad(
                id, idEspecialidad, idSede, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Datos actualizados correctamente.", actualizado));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO')")
    @Operation(summary = "Actualizar disponibilidad del médico")
    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<ApiResponse<MedicoResponseDTO>> actualizarDisponibilidad(
            @PathVariable Long id, @RequestParam Boolean disponible, Authentication authentication) {
        MedicoResponseDTO actualizado = medicoService.actualizarDisponibilidad(id, disponible,
                authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Disponibilidad actualizada correctamente.", actualizado));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL')")
    @Operation(summary = "Inactivar un médico")
    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<ApiResponse<Object>> inactivar(@PathVariable Long id, Authentication authentication) {
        medicoService.inactivar(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Médico inactivado correctamente.", null));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL')")
    @Operation(summary = "Activar un médico")
    @PatchMapping("/{id}/activar")
    public ResponseEntity<ApiResponse<Object>> activar(@PathVariable Long id, Authentication authentication) {
        medicoService.activar(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Médico activado correctamente.", null));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'RECEPCIONISTA')")
    @Operation(summary = "Consultar un médico por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicoResponseDTO>> consultar(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(medicoService.consultarPorId(id)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'RECEPCIONISTA')")
    @Operation(summary = "Listar todos los médicos")
    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicoResponseDTO>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.success(medicoService.listarTodos()));
    }

    @Operation(summary = "Buscar médicos disponibles por especialidad y/o sede (público)")
    @GetMapping("/disponibles")
    public ResponseEntity<ApiResponse<List<MedicoResponseDTO>>> buscarDisponibles(
            @RequestParam(required = false) Long idEspecialidad,
            @RequestParam(required = false) Long idSede) {
        return ResponseEntity.ok(ApiResponse.success(
                medicoService.listarDisponiblesPorEspecialidadYSede(idEspecialidad, idSede)));
    }

    @PreAuthorize("hasRole('MEDICO')")
    @Operation(summary = "Obtener mi información como médico")
    @GetMapping("/mi-perfil")
    public ResponseEntity<ApiResponse<MedicoResponseDTO>> obtenerMiPerfil(Authentication authentication) {

        Long idUsuario = (Long) authentication.getPrincipal();

        MedicoResponseDTO medico = medicoService.consultarPorIdUsuario(idUsuario);

        return ResponseEntity.ok(
                ApiResponse.success("Información del médico obtenida correctamente.", medico));
    }

    @GetMapping("/mis-pacientes")
    @PreAuthorize("hasRole('MEDICO')")
    @Operation(summary = "Listar pacientes atendidos por el médico autenticado")
    public ResponseEntity<ApiResponse<List<PacienteResponseDTO>>> misPacientes(
            Authentication authentication) {

        Long idUsuario = (Long) authentication.getPrincipal();

        return ResponseEntity.ok(
                ApiResponse.success(
                        medicoService.listarPacientesDelMedicoPorUsuario(idUsuario)));
    }
}