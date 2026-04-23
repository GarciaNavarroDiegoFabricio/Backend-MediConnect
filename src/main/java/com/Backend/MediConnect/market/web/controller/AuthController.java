package com.Backend.MediConnect.market.web.controller;

import com.Backend.MediConnect.market.domain.dto.*;
import com.Backend.MediConnect.market.domain.interfaces.IUsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IUsuarioService usuarioService;

    public AuthController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ─── LOGIN UNIFICADO ──────────────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(usuarioService.login(request));
    }

    // ─── REGISTRO ABIERTO (SOLO PACIENTES) ───────────────────────────────────
    @PostMapping("/registro/paciente")
    public ResponseEntity<AuthResponse> registrarPaciente(@RequestBody RegistroPacienteDTO dto) {
        return ResponseEntity.ok(usuarioService.registrarPaciente(dto));
    }

    // ─── REGISTRO ADMIN LOCAL (requiere JWT ADMIN_TOTAL) ─────────────────────
    @PostMapping("/registro/admin-local")
    public ResponseEntity<AuthResponse> registrarAdminLocal(@RequestBody RegistroAdminLocalDTO dto) {
        return ResponseEntity.ok(usuarioService.registrarAdminLocal(dto));
    }

    // ─── REGISTRO ADMIN TOTAL (requiere JWT ADMIN_TOTAL) ─────────────────────
    @PostMapping("/registro/admin-total")
    public ResponseEntity<AuthResponse> registrarAdminTotal(@RequestBody RegistroAdminTotalDTO dto) {
        return ResponseEntity.ok(usuarioService.registrarAdminTotal(dto));
    }

    // ─── REGISTRO MEDICO (requiere JWT ADMIN_LOCAL o ADMIN_TOTAL) ────────────
    @PostMapping("/registro/medico")
    public ResponseEntity<AuthResponse> registrarMedico(@RequestBody RegistroMedicoDTO dto) {
        return ResponseEntity.ok(usuarioService.registrarMedico(dto));
    }
}