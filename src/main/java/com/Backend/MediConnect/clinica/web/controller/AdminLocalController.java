package com.Backend.MediConnect.clinica.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Backend.MediConnect.clinica.domain.dto.HorarioDTO;
import com.Backend.MediConnect.clinica.domain.dto.HorarioResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.MedicoResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.ReprogramarHorarioDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.IAdminLocalService;

import java.util.List;

@RestController
@RequestMapping("/api/admin-local")
public class AdminLocalController {

    private final IAdminLocalService adminLocalService;

    public AdminLocalController(IAdminLocalService adminLocalService) {
        this.adminLocalService = adminLocalService;
    }

    @PostMapping("/horario")
    public ResponseEntity<HorarioResponseDTO> crearHorario(@RequestBody HorarioDTO dto) {
        return ResponseEntity.ok(adminLocalService.crearHorario(dto));
    }

    @PutMapping("/horario/{idHorario}/reprogramar")
    public ResponseEntity<HorarioResponseDTO> reprogramarHorario(@PathVariable Integer idHorario,
            @RequestBody ReprogramarHorarioDTO dto) {
        return ResponseEntity.ok(adminLocalService.reprogramarHorario(idHorario, dto));
    }

    @PutMapping("/horario/{idHorario}/cancelar")
    public ResponseEntity<Void> cancelarHorario(@PathVariable Integer idHorario) {
        adminLocalService.cancelarHorario(idHorario);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/horario/{idHorario}/bloquear")
    public ResponseEntity<Void> bloquearHorario(@PathVariable Integer idHorario) {
        adminLocalService.bloquearHorario(idHorario);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medicos/{idSede}")
    public ResponseEntity<List<MedicoResponseDTO>> gestionarMedico(@PathVariable Integer idSede) {
        return ResponseEntity.ok(adminLocalService.gestionarMedico(idSede));
    }
}