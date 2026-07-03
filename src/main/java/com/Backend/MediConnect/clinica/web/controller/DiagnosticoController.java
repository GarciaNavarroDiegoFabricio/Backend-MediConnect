package com.Backend.MediConnect.clinica.web.controller;

import org.springframework.web.bind.annotation.RestController;
import com.Backend.MediConnect.clinica.domain.services.DiagnosticoService;
import com.Backend.MediConnect.clinica.persistance.entity.Diagnostico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@PreAuthorize("hasRole('MEDICO')")
@RestController
@RequestMapping("/api/diagnosticos")
public class DiagnosticoController {

    @Autowired
    private DiagnosticoService diagnosticoService;

    // El medico podra registrar un diagnostico cuando realice una consulta con el
    // paciente
    @PostMapping("/consulta/{consultaId}")
    public ResponseEntity<Diagnostico> registrar(
            @PathVariable Integer consultaId,
            @RequestBody Diagnostico request) {

        return ResponseEntity.ok(
                diagnosticoService.registrarDiagnostico(
                        consultaId,
                        request.getDescripcion()));
    }
}