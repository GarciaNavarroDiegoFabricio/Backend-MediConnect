package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.request.BloqueoHorarioRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.ApiResponse;
import com.Backend.MediConnect.clinica.domain.dto.response.BloqueoHorarioResponseDTO;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.repository.IMedicoRepository;
import com.Backend.MediConnect.clinica.domain.services.BloqueoHorarioService;
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
@RequestMapping("/api/bloqueos-horario")
@Tag(name = "Bloqueos de Horario", description = "Vacaciones, permisos o emergencias de médicos")
@SecurityRequirement(name = "bearerAuth")
public class BloqueoHorarioController {

    private final BloqueoHorarioService bloqueoHorarioService;
    private final IMedicoRepository medicoRepository;

    public BloqueoHorarioController(BloqueoHorarioService bloqueoHorarioService, IMedicoRepository medicoRepository) {
        this.bloqueoHorarioService = bloqueoHorarioService;
        this.medicoRepository = medicoRepository;
    }

    @PreAuthorize("hasRole('MEDICO')")
    @Operation(summary = "Crear un bloqueo propio (médico autenticado)")
    @PostMapping("/mi-bloqueo")
    public ResponseEntity<ApiResponse<BloqueoHorarioResponseDTO>> crearMiBloqueo(
            @Valid @RequestBody BloqueoHorarioRequestDTO request, Authentication authentication) {
        Long idUsuario = (Long) authentication.getPrincipal();
        Long idMedico = obtenerIdMedicoPorUsuario(idUsuario);
        BloqueoHorarioResponseDTO creado = bloqueoHorarioService.crear(idMedico, request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Bloqueo registrado correctamente.", creado));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO')")
    @Operation(summary = "Crear un bloqueo para un médico por ID")
    @PostMapping("/{idMedico}")
    public ResponseEntity<ApiResponse<BloqueoHorarioResponseDTO>> crear(
            @PathVariable Long idMedico, @Valid @RequestBody BloqueoHorarioRequestDTO request, Authentication authentication) {
        BloqueoHorarioResponseDTO creado = bloqueoHorarioService.crear(idMedico, request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Bloqueo registrado correctamente.", creado));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO')")
    @Operation(summary = "Eliminar un bloqueo")
    @DeleteMapping("/{idBloqueo}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable Long idBloqueo) {
        bloqueoHorarioService.eliminar(idBloqueo);
        return ResponseEntity.ok(ApiResponse.success("Bloqueo eliminado correctamente.", null));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'RECEPCIONISTA', 'MEDICO')")
    @Operation(summary = "Listar bloqueos de un médico por ID")
    @GetMapping("/{idMedico}")
    public ResponseEntity<ApiResponse<List<BloqueoHorarioResponseDTO>>> listarPorMedico(@PathVariable Long idMedico) {
        return ResponseEntity.ok(ApiResponse.success(bloqueoHorarioService.listarPorMedico(idMedico)));
    }

    private Long obtenerIdMedicoPorUsuario(Long idUsuario) {
        return medicoRepository.findByPersona_Usuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado para este usuario."))
                .getIdMedico();
    }
}