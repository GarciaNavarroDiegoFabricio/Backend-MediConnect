package com.Backend.MediConnect.clinica.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Backend.MediConnect.clinica.domain.dto.EspecialidadResponseDTO;
import com.Backend.MediConnect.clinica.domain.repository.EspecialidadRepository;
import com.Backend.MediConnect.clinica.web.mapper.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    private final EspecialidadRepository especialidadRepo;

    public EspecialidadController(EspecialidadRepository especialidadRepo) {
        this.especialidadRepo = especialidadRepo;
    }

    @GetMapping
    public ResponseEntity<List<EspecialidadResponseDTO>> listarEspecialidades() {
        return ResponseEntity.ok(
                especialidadRepo.findAll()
                        .stream()
                        .map(EntityMapper::toEspecialidadResponse)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDTO> obtenerEspecialidad(@PathVariable Integer id) {
        return especialidadRepo.findById(id)
                .map(e -> ResponseEntity.ok(EntityMapper.toEspecialidadResponse(e)))
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
    }
}