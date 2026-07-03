package com.Backend.MediConnect.clinica.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.Backend.MediConnect.clinica.domain.dto.CitaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.PacienteBusquedaDTO;
import com.Backend.MediConnect.clinica.domain.dto.RecetaDTO;
import com.Backend.MediConnect.clinica.domain.dto.RecetaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.ReporteResponseDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.IMedicoService;
import com.Backend.MediConnect.clinica.domain.interfaces.IPacienteConsultaService;

import java.util.List;

@RestController
@RequestMapping("/api/medico")
public class MedicoController {

    private final IMedicoService medicoService;
    private final IPacienteConsultaService pacienteConsultaService;

    public MedicoController(IMedicoService medicoService, IPacienteConsultaService pacienteConsultaService) {
        this.medicoService = medicoService;
        this.pacienteConsultaService = pacienteConsultaService;
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

    @PatchMapping("/consulta/{idConsulta}/finalizar")
    public ResponseEntity<Void> finalizarConsulta(@PathVariable Integer idConsulta) {
        pacienteConsultaService.finalizarConsulta(idConsulta);
        return ResponseEntity.noContent().build();
    }
}