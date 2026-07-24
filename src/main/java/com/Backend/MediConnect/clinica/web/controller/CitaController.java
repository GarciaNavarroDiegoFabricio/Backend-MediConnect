package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.request.CitaCancelarRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.CitaRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.CitaReprogramarRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.ApiResponse;
import com.Backend.MediConnect.clinica.domain.dto.response.CitaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.HistorialCitaResponseDTO;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.repository.IMedicoRepository;
import com.Backend.MediConnect.clinica.domain.services.CitaService;
import com.Backend.MediConnect.clinica.persistance.entity.Medico;

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
@RequestMapping("/api/citas")
@Tag(name = "Citas", description = "Reserva, reprogramación, cancelación y consulta de citas médicas")
@SecurityRequirement(name = "bearerAuth")
public class CitaController {

    private final CitaService citaService;
    private final IMedicoRepository medicoRepository;

    public CitaController(CitaService citaService, IMedicoRepository medicoRepository) {
        this.citaService = citaService;
        this.medicoRepository = medicoRepository;
    }

    @PreAuthorize("hasRole('PACIENTE')")
    @Operation(summary = "Reservar una cita médica tras confirmación del pago")
    @PostMapping
    public ResponseEntity<ApiResponse<CitaResponseDTO>> reservar(
            @Valid @RequestBody CitaRequestDTO request, Authentication authentication) {
        Long idUsuario = (Long) authentication.getPrincipal();
        CitaResponseDTO creada = citaService.reservar(idUsuario, request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Cita reservada correctamente.", creada));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'RECEPCIONISTA', 'PACIENTE')")
    @Operation(summary = "Reprogramar una cita")
    @PatchMapping("/{id}/reprogramar")
    public ResponseEntity<ApiResponse<CitaResponseDTO>> reprogramar(
            @PathVariable Long id, @Valid @RequestBody CitaReprogramarRequestDTO request,
            Authentication authentication) {
        CitaResponseDTO actualizada = citaService.reprogramar(id, request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Cita reprogramada correctamente.", actualizada));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'RECEPCIONISTA', 'PACIENTE')")
    @Operation(summary = "Cancelar una cita")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ApiResponse<Object>> cancelar(
            @PathVariable Long id, @Valid @RequestBody CitaCancelarRequestDTO request, Authentication authentication) {
        citaService.cancelar(id, request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Cita cancelada correctamente.", null));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO')")
    @Operation(summary = "Marcar una cita como atendida")
    @PatchMapping("/{id}/atendida")
    public ResponseEntity<ApiResponse<CitaResponseDTO>> marcarComoAtendida(
            @PathVariable Long id, Authentication authentication) {
        CitaResponseDTO actualizada = citaService.marcarComoAtendida(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Cita marcada como atendida.", actualizada));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO')")
    @Operation(summary = "Marcar una cita como no asistió")
    @PatchMapping("/{id}/no-asistio")
    public ResponseEntity<ApiResponse<CitaResponseDTO>> marcarComoNoAsistio(
            @PathVariable Long id, Authentication authentication) {
        CitaResponseDTO actualizada = citaService.marcarComoNoAsistio(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Cita marcada como no asistió.", actualizada));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'RECEPCIONISTA', 'MEDICO', 'PACIENTE')")
    @Operation(summary = "Consultar una cita por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CitaResponseDTO>> consultar(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(citaService.consultarPorId(id)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'RECEPCIONISTA', 'MEDICO', 'PACIENTE')")
    @Operation(summary = "Consultar el historial de cambios de una cita")
    @GetMapping("/{id}/historial")
    public ResponseEntity<ApiResponse<List<HistorialCitaResponseDTO>>> consultarHistorial(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(citaService.consultarHistorial(id)));
    }

    @PreAuthorize("hasRole('MEDICO')")
    @GetMapping("/mis-citas")
    public ResponseEntity<ApiResponse<List<CitaResponseDTO>>> listarMisCitas(
            Authentication authentication) {

        Long idUsuario = (Long) authentication.getPrincipal();

        return ResponseEntity.ok(
                ApiResponse.success(
                        citaService.listarPorMedicoUsuario(idUsuario)));

    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'RECEPCIONISTA')")
    @Operation(summary = "Listar citas de un médico")
    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<ApiResponse<List<CitaResponseDTO>>> listarPorMedico(@PathVariable Long idMedico) {
        return ResponseEntity.ok(ApiResponse.success(citaService.listarPorMedico(idMedico)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'RECEPCIONISTA')")
    @Operation(summary = "Listar citas por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<CitaResponseDTO>>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(ApiResponse.success(citaService.listarPorEstado(estado)));
    }

}