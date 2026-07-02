package com.Backend.MediConnect.clinica.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; //  AGREGA ESTO
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Backend.MediConnect.clinica.domain.dto.EspecialidadResponseDTO;
import com.Backend.MediConnect.clinica.domain.repository.EspecialidadRepository;
import com.Backend.MediConnect.clinica.web.mapper.MantenimientoMapper;

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
                        .map(MantenimientoMapper::toEspecialidadResponse)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDTO> obtenerEspecialidad(@PathVariable Integer id) {
        return especialidadRepo.findById(id)
                .map(e -> ResponseEntity.ok(MantenimientoMapper.toEspecialidadResponse(e)))
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
    }
}