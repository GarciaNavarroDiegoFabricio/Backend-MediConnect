package com.Backend.MediConnect.clinica.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Backend.MediConnect.clinica.domain.dto.SedeRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.SedeResponseDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.ISedeService;

@RestController
@RequestMapping("/api/sedes")
public class SedeController {

    private final ISedeService sedeService;

    public SedeController(ISedeService sedeService) {
        this.sedeService = sedeService;
    }

    @GetMapping
    public ResponseEntity<List<SedeResponseDTO>> listarSedes() {
        return ResponseEntity.ok(sedeService.listarSedes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SedeResponseDTO> obtenerSede(@PathVariable Integer id) {
        return ResponseEntity.ok(sedeService.obtenerSede(id));
    }

    @PostMapping
    public ResponseEntity<SedeResponseDTO> registrarSede(@RequestBody SedeRequestDTO dto) {
        return ResponseEntity.ok(sedeService.registrarSede(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SedeResponseDTO> actualizarSede(@PathVariable Integer id,
                                                          @RequestBody SedeRequestDTO dto) {
        return ResponseEntity.ok(sedeService.actualizarSede(id, dto));
    }

    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<Void> inactivarSede(@PathVariable Integer id) {
        sedeService.inactivarSede(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activarSede(@PathVariable Integer id) {
        sedeService.activarSede(id);
        return ResponseEntity.noContent().build();
    }
}