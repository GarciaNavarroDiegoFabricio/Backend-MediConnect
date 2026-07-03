package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.ConsultaContextoDTO;
import com.Backend.MediConnect.clinica.domain.services.ConsultaService;
import com.Backend.MediConnect.clinica.persistance.entity.Consulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @PostMapping("/iniciar/{citaId}")
    public ResponseEntity<Consulta> iniciar(@PathVariable Integer citaId) {
        return ResponseEntity.ok(consultaService.iniciarConsulta(citaId));
    }

    @PutMapping("/finalizar/{id}")
    public ResponseEntity<Consulta> finalizar(@PathVariable Integer id) {
        return ResponseEntity.ok(consultaService.finalizarConsulta(id));
    }

    // SOLO MÉDICOS PUEDEN VER CONTEXTO DE CONSULTA
    @PreAuthorize("hasRole('MEDICO')")
    @GetMapping("/{consultaId}/contexto")
    public ResponseEntity<ConsultaContextoDTO> obtenerContexto(
            @PathVariable Integer consultaId) {

        return ResponseEntity.ok(
                consultaService.obtenerContexto(consultaId));
    }
}