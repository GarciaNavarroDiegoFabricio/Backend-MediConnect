package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.request.*;
import com.Backend.MediConnect.clinica.domain.dto.response.*;
import com.Backend.MediConnect.clinica.domain.services.HistoriaClinicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/historias-clinicas")
@Tag(name = "Historia Clínica", description = "Gestión de historias clínicas, atenciones médicas y documentos")
@SecurityRequirement(name = "bearerAuth")
public class HistoriaClinicaController {

    private final HistoriaClinicaService historiaClinicaService;

    public HistoriaClinicaController(HistoriaClinicaService historiaClinicaService) {
        this.historiaClinicaService = historiaClinicaService;
    }

    @PreAuthorize("hasRole('MEDICO')")
    @Operation(summary = "Iniciar una atención médica a partir de una cita")
    @PostMapping("/atenciones/{idCita}/iniciar")
    public ResponseEntity<ApiResponse<AtencionMedicaResponseDTO>> iniciarAtencion(
            @PathVariable Long idCita, Authentication authentication) {
        AtencionMedicaResponseDTO atencion = historiaClinicaService.iniciarAtencion(idCita, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Atención médica iniciada correctamente.", atencion));
    }

    @PreAuthorize("hasRole('MEDICO')")
    @Operation(summary = "Registrar los signos vitales de una atención")
    @PostMapping("/atenciones/{idAtencion}/signos-vitales")
    public ResponseEntity<ApiResponse<Object>> registrarSignoVital(
            @PathVariable Long idAtencion, @Valid @RequestBody SignoVitalRequestDTO request) {
        historiaClinicaService.registrarSignoVital(idAtencion, request);
        return ResponseEntity.ok(ApiResponse.success("Signos vitales registrados correctamente.", null));
    }

    @PreAuthorize("hasRole('MEDICO')")
    @Operation(summary = "Registrar un diagnóstico en una atención")
    @PostMapping("/atenciones/{idAtencion}/diagnosticos")
    public ResponseEntity<ApiResponse<DiagnosticoResponseDTO>> registrarDiagnostico(
            @PathVariable Long idAtencion, @Valid @RequestBody DiagnosticoRequestDTO request) {
        DiagnosticoResponseDTO diagnostico = historiaClinicaService.registrarDiagnostico(idAtencion, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Diagnóstico registrado correctamente.", diagnostico));
    }

    @PreAuthorize("hasRole('MEDICO')")
    @Operation(summary = "Registrar un tratamiento en una atención")
    @PostMapping("/atenciones/{idAtencion}/tratamientos")
    public ResponseEntity<ApiResponse<TratamientoResponseDTO>> registrarTratamiento(
            @PathVariable Long idAtencion, @Valid @RequestBody TratamientoRequestDTO request) {
        TratamientoResponseDTO tratamiento = historiaClinicaService.registrarTratamiento(idAtencion, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tratamiento registrado correctamente.", tratamiento));
    }

    @PreAuthorize("hasRole('MEDICO')")
    @Operation(summary = "Cerrar una atención médica")
    @PatchMapping("/atenciones/{idAtencion}/cerrar")
    public ResponseEntity<ApiResponse<AtencionMedicaResponseDTO>> cerrarAtencion(
            @PathVariable Long idAtencion, @RequestBody AtencionMedicaCierreRequestDTO request) {
        AtencionMedicaResponseDTO atencion = historiaClinicaService.cerrarAtencion(idAtencion, request);
        return ResponseEntity.ok(ApiResponse.success("Atención médica cerrada correctamente.", atencion));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO', 'PACIENTE')")
    @Operation(summary = "Generar y enviar la constancia de una atención médica")
    @GetMapping("/atenciones/{idAtencion}/constancia")
    public ResponseEntity<byte[]> generarConstancia(@PathVariable Long idAtencion) {
        byte[] pdf = historiaClinicaService.generarYEnviarConstancia(idAtencion);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=constancia_" + idAtencion + ".pdf")
                .body(pdf);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO', 'RECEPCIONISTA')")
    @Operation(summary = "Registrar un antecedente clínico de un paciente")
    @PostMapping("/pacientes/{idPaciente}/antecedentes")
    public ResponseEntity<ApiResponse<AntecedenteClinicoResponseDTO>> registrarAntecedente(
            @PathVariable Long idPaciente, @Valid @RequestBody AntecedenteClinicoRequestDTO request,
            Authentication authentication) {
        AntecedenteClinicoResponseDTO antecedente = historiaClinicaService.registrarAntecedente(
                idPaciente, request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Antecedente registrado correctamente.", antecedente));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO', 'RECEPCIONISTA')")
    @Operation(summary = "Subir un documento clínico al expediente del paciente")
    @PostMapping(value = "/pacientes/{idPaciente}/documentos", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<DocumentoClinicoResponseDTO>> subirDocumento(
            @PathVariable Long idPaciente,
            @RequestParam(required = false) Long idAtencion,
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("tipoDocumento") String tipoDocumento,
            Authentication authentication) {
        DocumentoClinicoResponseDTO documento = historiaClinicaService.subirDocumento(
                idPaciente, idAtencion, archivo, tipoDocumento, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Documento clínico subido correctamente.", documento));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO', 'RECEPCIONISTA')")
    @Operation(summary = "Actualizar el tipo de un documento clínico")
    @PutMapping("/documentos/{idDocumento}")
    public ResponseEntity<ApiResponse<DocumentoClinicoResponseDTO>> actualizarDocumento(
            @PathVariable Long idDocumento, @Valid @RequestBody DocumentoClinicoRequestDTO request) {
        DocumentoClinicoResponseDTO documento = historiaClinicaService.actualizarDocumento(idDocumento, request);
        return ResponseEntity.ok(ApiResponse.success("Documento clínico actualizado correctamente.", documento));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO', 'RECEPCIONISTA')")
    @Operation(summary = "Reemplazar el archivo de un documento clínico")
    @PutMapping(value = "/documentos/{idDocumento}/archivo", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<DocumentoClinicoResponseDTO>> reemplazarArchivo(
            @PathVariable Long idDocumento, @RequestParam("archivo") MultipartFile archivo) {
        DocumentoClinicoResponseDTO documento = historiaClinicaService.reemplazarArchivoDocumento(idDocumento, archivo);
        return ResponseEntity.ok(ApiResponse.success("Archivo del documento reemplazado correctamente.", documento));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO', 'RECEPCIONISTA')")
    @Operation(summary = "Eliminar un documento clínico")
    @DeleteMapping("/documentos/{idDocumento}")
    public ResponseEntity<ApiResponse<Object>> eliminarDocumento(@PathVariable Long idDocumento) {
        historiaClinicaService.eliminarDocumento(idDocumento);
        return ResponseEntity.ok(ApiResponse.success("Documento clínico eliminado correctamente.", null));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO', 'RECEPCIONISTA', 'PACIENTE')")
    @Operation(summary = "Consultar un documento clínico por ID")
    @GetMapping("/documentos/{idDocumento}")
    public ResponseEntity<ApiResponse<DocumentoClinicoResponseDTO>> consultarDocumento(@PathVariable Long idDocumento) {
        return ResponseEntity.ok(ApiResponse.success(historiaClinicaService.consultarDocumentoPorId(idDocumento)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO', 'RECEPCIONISTA', 'PACIENTE')")
    @Operation(summary = "Listar los documentos clínicos de un paciente")
    @GetMapping("/pacientes/{idPaciente}/documentos")
    public ResponseEntity<ApiResponse<List<DocumentoClinicoResponseDTO>>> listarDocumentosPorPaciente(
            @PathVariable Long idPaciente) {
        return ResponseEntity.ok(ApiResponse.success(historiaClinicaService.listarDocumentosPorPaciente(idPaciente)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO', 'RECEPCIONISTA')")
    @Operation(summary = "Listar los documentos clínicos de una atención médica")
    @GetMapping("/atenciones/{idAtencion}/documentos")
    public ResponseEntity<ApiResponse<List<DocumentoClinicoResponseDTO>>> listarDocumentosPorAtencion(
            @PathVariable Long idAtencion) {
        return ResponseEntity.ok(ApiResponse.success(historiaClinicaService.listarDocumentosPorAtencion(idAtencion)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR_TOTAL', 'ADMINISTRADOR_LOCAL', 'MEDICO', 'PACIENTE')")
    @Operation(summary = "Consultar la historia clínica completa de un paciente")
    @GetMapping("/pacientes/{idPaciente}")
    public ResponseEntity<ApiResponse<HistoriaClinicaResponseDTO>> consultarPorPaciente(
            @PathVariable Long idPaciente) {
        return ResponseEntity.ok(ApiResponse.success(historiaClinicaService.consultarPorPaciente(idPaciente)));
    }
}