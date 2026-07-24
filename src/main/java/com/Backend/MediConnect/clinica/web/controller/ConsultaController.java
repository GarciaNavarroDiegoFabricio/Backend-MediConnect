package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.request.DiagnosticoMedicoRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.SignoVitalRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.ApiResponse;
import com.Backend.MediConnect.clinica.domain.dto.response.ConsultaInicioResponseDTO;
import com.Backend.MediConnect.clinica.domain.services.ConsultaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.Backend.MediConnect.clinica.domain.dto.request.DetalleAtencionRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.DetalleRecetaRequestDTO;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultas")
@Tag(name = "Consultas médicas")
@SecurityRequirement(name = "bearerAuth")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PreAuthorize("hasRole('MEDICO')")
    @PostMapping("/iniciar/{idCita}")
    public ResponseEntity<ApiResponse<ConsultaInicioResponseDTO>> iniciar(
            @PathVariable Long idCita) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Consulta iniciada correctamente.",
                        consultaService.iniciarConsulta(idCita)));

    }

    @PostMapping("/{idConsulta}/signos-vitales")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<ApiResponse<Object>> registrarSignosVitales(
            @PathVariable Long idConsulta,
            @RequestBody @Valid SignoVitalRequestDTO request) {

        consultaService.registrarSignosVitales(idConsulta, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Signos vitales registrados correctamente.",
                        null));
    }

    @PostMapping("/{idConsulta}/diagnostico")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<ApiResponse<Object>> registrarDiagnostico(
            @PathVariable Long idConsulta,
            @Valid @RequestBody DiagnosticoMedicoRequestDTO request) {

        consultaService.registrarDiagnostico(idConsulta, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Diagnóstico registrado correctamente.",
                        null));
    }

    @PostMapping("/{idConsulta}/detalle-atencion")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<ApiResponse<Object>> registrarDetalleAtencion(
            @PathVariable Long idConsulta,
            @Valid @RequestBody DetalleAtencionRequestDTO request) {

        consultaService.registrarDetalleAtencion(idConsulta, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Detalle de atención registrado correctamente.",
                        null));
    }

    @PostMapping("/{idConsulta}/receta")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<ApiResponse<Object>> registrarReceta(
            @PathVariable Long idConsulta,
            @Valid @RequestBody List<DetalleRecetaRequestDTO> request) {

        consultaService.registrarReceta(idConsulta, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Receta registrada correctamente.",
                        null));
    }

    @PatchMapping("/{idConsulta}/finalizar")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<ApiResponse<Object>> finalizarConsulta(
            @PathVariable Long idConsulta) {

        consultaService.finalizarConsulta(idConsulta);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Consulta finalizada correctamente.",
                        null));
    }

}