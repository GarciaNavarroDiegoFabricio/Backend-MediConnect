package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.request.HorarioMedicoRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.ApiResponse;
import com.Backend.MediConnect.clinica.domain.dto.response.HorarioMedicoResponseDTO;
import com.Backend.MediConnect.clinica.domain.repository.IMedicoRepository;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.services.HorarioMedicoService;
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
@RequestMapping("/api/horarios-medico")
@Tag(name = "Horarios Médicos", description = "Gestión de horarios semanales de médicos")
@SecurityRequirement(name = "bearerAuth")
public class HorarioMedicoController {

    private final HorarioMedicoService horarioMedicoService;
    private final IMedicoRepository medicoRepository;

    public HorarioMedicoController(HorarioMedicoService horarioMedicoService, IMedicoRepository medicoRepository) {
        this.horarioMedicoService = horarioMedicoService;
        this.medicoRepository = medicoRepository;
    }

    @PreAuthorize("hasRole('MEDICO')")
    @Operation(summary = "Crear un horario propio (médico autenticado)")
    @PostMapping("/mi-horario")
    public ResponseEntity<ApiResponse<HorarioMedicoResponseDTO>> crearMiHorario(
            @Valid @RequestBody HorarioMedicoRequestDTO request, Authentication authentication) {
        Long idUsuario = (Long) authentication.getPrincipal();
        Long idMedico = obtenerIdMedicoPorUsuario(idUsuario);
        HorarioMedicoResponseDTO creado = horarioMedicoService.crear(idMedico, request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Horario creado correctamente.", creado));
    }

    @PreAuthorize("hasRole('MEDICO')")
    @Operation(summary = "Listar mis propios horarios")
    @GetMapping("/mi-horario")
    public ResponseEntity<ApiResponse<List<HorarioMedicoResponseDTO>>> listarMiHorario(Authentication authentication) {
        Long idUsuario = (Long) authentication.getPrincipal();
        Long idMedico = obtenerIdMedicoPorUsuario(idUsuario);
        return ResponseEntity.ok(ApiResponse.success(horarioMedicoService.listarPorMedico(idMedico)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO')")
    @Operation(summary = "Crear un horario para un médico por ID")
    @PostMapping("/{idMedico}")
    public ResponseEntity<ApiResponse<HorarioMedicoResponseDTO>> crear(
            @PathVariable Long idMedico, @Valid @RequestBody HorarioMedicoRequestDTO request, Authentication authentication) {
        HorarioMedicoResponseDTO creado = horarioMedicoService.crear(idMedico, request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Horario creado correctamente.", creado));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO')")
    @Operation(summary = "Actualizar un horario")
    @PutMapping("/{idHorario}")
    public ResponseEntity<ApiResponse<HorarioMedicoResponseDTO>> actualizar(
            @PathVariable Long idHorario, @Valid @RequestBody HorarioMedicoRequestDTO request, Authentication authentication) {
        HorarioMedicoResponseDTO actualizado = horarioMedicoService.actualizar(idHorario, request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Horario actualizado correctamente.", actualizado));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO')")
    @Operation(summary = "Inactivar un horario")
    @PatchMapping("/{idHorario}/inactivar")
    public ResponseEntity<ApiResponse<Object>> inactivar(@PathVariable Long idHorario, Authentication authentication) {
        horarioMedicoService.inactivar(idHorario, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Horario inactivado correctamente.", null));
    }

    @Operation(summary = "Listar horarios de un médico por ID (público)")
    @GetMapping("/{idMedico}")
    public ResponseEntity<ApiResponse<List<HorarioMedicoResponseDTO>>> listarPorMedico(@PathVariable Long idMedico) {
        return ResponseEntity.ok(ApiResponse.success(horarioMedicoService.listarPorMedico(idMedico)));
    }

    private Long obtenerIdMedicoPorUsuario(Long idUsuario) {
        return medicoRepository.findByPersona_Usuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado para este usuario."))
                .getIdMedico();
    }
}