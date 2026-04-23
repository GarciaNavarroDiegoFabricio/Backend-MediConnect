package com.Backend.MediConnect.market.web.controller;

import com.Backend.MediConnect.market.domain.dto.EspecialidadResponseDTO;
import com.Backend.MediConnect.market.web.mapper.EntityMapper;
import com.Backend.MediConnect.market.domain.repository.EspecialidadRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDTO> obtenerEspecialidad(@PathVariable Integer id) {
        return especialidadRepo.findById(id)
                .map(e -> ResponseEntity.ok(EntityMapper.toEspecialidadResponse(e)))
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
    }
}