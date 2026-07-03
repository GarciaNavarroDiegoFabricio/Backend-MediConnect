package com.Backend.MediConnect.clinica.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.Backend.MediConnect.clinica.domain.dto.CitaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.ConsultaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.PacienteBusquedaDTO;
import com.Backend.MediConnect.clinica.domain.dto.RecetaDTO;
import com.Backend.MediConnect.clinica.domain.dto.RecetaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.ReporteResponseDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.IMedicoService;

import java.util.List;

@PreAuthorize("hasRole('MEDICO')")
@RestController
@RequestMapping("/api/medico")
public class MedicoController {

    private final IMedicoService medicoService;

    public MedicoController(IMedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PutMapping("/disponibilidad")
    public ResponseEntity<Void> cambiarDisponibilidad(@RequestParam Boolean disponible, Authentication auth) {
        medicoService.cambiarDisponibilidad(auth.getName(), disponible);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reporte")
    public ResponseEntity<ReporteResponseDTO> generarReporte(Authentication auth) {
        return ResponseEntity.ok(medicoService.generarReporteConsulta(auth.getName()));
    }

    @PostMapping("/receta")
    public ResponseEntity<RecetaResponseDTO> crearReceta(@RequestBody RecetaDTO dto, Authentication auth) {
        return ResponseEntity.ok(medicoService.crearReceta(auth.getName(), dto));
    }

    @GetMapping("/reservas")
    public ResponseEntity<List<CitaResponseDTO>> consultarReservas(Authentication auth) {
        return ResponseEntity.ok(medicoService.consultarReservas(auth.getName()));
    }

    @GetMapping("/pacientes/buscar")
    public ResponseEntity<List<PacienteBusquedaDTO>> buscarPacientes(@RequestParam String termino) {
        List<PacienteBusquedaDTO> resultados = medicoService.buscarPacientes(termino);
        return ResponseEntity.ok(resultados);
    }

    // Permite al medico colocar que está en horario de una cita programada que ya
    // debería haber empezado
    // pero está esperando al paciente a que llegue. Esto indica que no está
    // disponible ahora mismo.
    @PatchMapping("/citas/{id}/en-espera")
    public ResponseEntity<Void> ponerEnEspera(
            @PathVariable Integer id,
            Authentication auth) {

        medicoService.ponerEnEspera(id, auth.getName());

        return ResponseEntity.ok().build();
    }

    // Esto indica que el medico ha comenzado la consulta con el paciente
    @PostMapping("/consultas/comenzar/{idCita}")
    public ResponseEntity<ConsultaResponseDTO> comenzarConsulta(
            @PathVariable Integer idCita, Authentication auth) {

        return ResponseEntity.ok(
                medicoService.comenzarConsulta(idCita, auth.getName()));

    }

    // Esto indica que la consulta ha terminado y se guardan los datos de la
    // consulta, el diagnostico y la fecha de finalizacion de la consulta
    @PatchMapping("/consultas/{id}/terminar")
    public ResponseEntity<ConsultaResponseDTO> terminarConsulta(
            @PathVariable Integer id, Authentication auth) {

        return ResponseEntity.ok(
                medicoService.terminarConsulta(id, auth.getName()));

    }

}