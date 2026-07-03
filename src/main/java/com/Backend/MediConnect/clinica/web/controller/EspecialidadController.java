package com.Backend.MediConnect.clinica.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Backend.MediConnect.clinica.domain.dto.EspecialidadRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.EspecialidadResponseDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.IEspecialidadService;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    private final IEspecialidadService especialidadService;

    public EspecialidadController(IEspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    @GetMapping
    public ResponseEntity<List<EspecialidadResponseDTO>> listarEspecialidades() {
        return ResponseEntity.ok(especialidadService.listarEspecialidades());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDTO> obtenerEspecialidad(@PathVariable Integer id) {
        return ResponseEntity.ok(especialidadService.obtenerEspecialidad(id));
    }

    @PostMapping
    public ResponseEntity<EspecialidadResponseDTO> registrarEspecialidad(@RequestBody EspecialidadRequestDTO dto) {
        return ResponseEntity.ok(especialidadService.registrarEspecialidad(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDTO> actualizarEspecialidad(@PathVariable Integer id,
                                                                          @RequestBody EspecialidadRequestDTO dto) {
        return ResponseEntity.ok(especialidadService.actualizarEspecialidad(id, dto));
    }

    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<Void> inactivarEspecialidad(@PathVariable Integer id) {
        especialidadService.inactivarEspecialidad(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activarEspecialidad(@PathVariable Integer id) {
        especialidadService.activarEspecialidad(id);
        return ResponseEntity.noContent().build();
    }
}