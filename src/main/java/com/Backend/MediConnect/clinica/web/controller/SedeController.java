package com.Backend.MediConnect.clinica.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; //  AGREGA ESTO
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Backend.MediConnect.clinica.domain.dto.SedeResponseDTO;
import com.Backend.MediConnect.clinica.domain.repository.SedeRepository;
import com.Backend.MediConnect.clinica.web.mapper.MantenimientoMapper;

@RestController
@RequestMapping("/api/sedes")
public class SedeController {

    private final SedeRepository sedeRepo;

    public SedeController(SedeRepository sedeRepo) {
        this.sedeRepo = sedeRepo;
    }

    @GetMapping
    public ResponseEntity<List<SedeResponseDTO>> listarSedes() {
        return ResponseEntity.ok(
                sedeRepo.findAll()
                        .stream()
                        .map(MantenimientoMapper::toSedeResponse)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SedeResponseDTO> obtenerSede(@PathVariable Integer id) {
        return sedeRepo.findById(id)
                .map(s -> ResponseEntity.ok(MantenimientoMapper.toSedeResponse(s)))
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));
    }
}