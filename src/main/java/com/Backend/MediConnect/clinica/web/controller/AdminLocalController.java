package com.Backend.MediConnect.clinica.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin; //  AGREGADO
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Backend.MediConnect.clinica.domain.dto.BloquearHorarioDTO;
import com.Backend.MediConnect.clinica.domain.dto.EditarHorarioDTO;
import com.Backend.MediConnect.clinica.domain.dto.HorarioDTO;
import com.Backend.MediConnect.clinica.domain.dto.HorarioResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.MedicoResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.ReprogramarHorarioDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.IAdminLocalService;

@CrossOrigin(origins = "*") // AGREGADO: Permite que cualquier puerto del frontend (React, Angular, etc.) consuma estos endpoints
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
    public ResponseEntity<Void> bloquearHorario(@PathVariable Integer idHorario, 
            @RequestBody BloquearHorarioDTO dto) {
        adminLocalService.bloquearHorario(idHorario, dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medicos/{idSede}")
    public ResponseEntity<List<MedicoResponseDTO>> gestionarMedico(@PathVariable Integer idSede) {
        return ResponseEntity.ok(adminLocalService.gestionarMedico(idSede));
    }

    @PutMapping("/medicos/{idMedico}/estado")
    public ResponseEntity<Void> cambiarEstadoMedico(@PathVariable Integer idMedico, @RequestParam String estado) {
        adminLocalService.cambiarEstadoMedico(idMedico, estado);
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // ENDPOINTS AGREGADOS: INTEGRANTE 3 - RF1
    // ==========================================
    @PutMapping("/horario/{idHorario}")
    public ResponseEntity<HorarioResponseDTO> actualizarHorario(@PathVariable Integer idHorario,
            @RequestBody EditarHorarioDTO dto) {
        return ResponseEntity.ok(adminLocalService.actualizarHorario(idHorario, dto));
    }

    @PutMapping("/horario/{idHorario}/inactivar")
    public ResponseEntity<Void> inactivarHorario(@PathVariable Integer idHorario) {
        adminLocalService.inactivarHorario(idHorario);
        return ResponseEntity.noContent().build();
    }
}