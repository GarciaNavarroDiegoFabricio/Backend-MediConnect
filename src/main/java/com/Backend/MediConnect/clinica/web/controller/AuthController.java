package com.Backend.MediConnect.clinica.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.Backend.MediConnect.clinica.domain.dto.*;
import com.Backend.MediConnect.clinica.domain.interfaces.IUsuarioService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(usuarioService.login(request));
    }

    @PostMapping("/registro/paciente")
    public ResponseEntity<AuthResponse> registrarPaciente(@RequestBody RegistroPacienteDTO dto) {
        return ResponseEntity.ok(usuarioService.registrarPaciente(dto));
    }

    @PostMapping("/registro/admin-local")
    public ResponseEntity<AuthResponse> registrarAdminLocal(@RequestBody RegistroAdminLocalDTO dto) {
        return ResponseEntity.ok(usuarioService.registrarAdminLocal(dto));
    }

    @PostMapping("/registro/admin-total")
    public ResponseEntity<AuthResponse> registrarAdminTotal(@RequestBody RegistroAdminTotalDTO dto) {
        return ResponseEntity.ok(usuarioService.registrarAdminTotal(dto));
    }

    @PostMapping("/registro/medico")
    public ResponseEntity<AuthResponse> registrarMedico(@RequestBody RegistroMedicoDTO dto, Authentication auth) {
        String dni = auth.getName();
        String rol = auth.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok(usuarioService.registrarMedico(dto, dni, rol));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioPerfilDTO> obtenerPerfil(Authentication auth) {
        String dni = auth.getName();
        String rol = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        return ResponseEntity.ok(usuarioService.obtenerPerfil(dni, rol));
    }

    @GetMapping("/medicos")
    public ResponseEntity<List<UsuarioPerfilDTO>> listarMedicos() {
        return ResponseEntity.ok(usuarioService.listarMedicos());
    }

    @GetMapping("/pacientes")
    public ResponseEntity<List<UsuarioPerfilDTO>> listarPacientes() {
        return ResponseEntity.ok(usuarioService.listarPacientes());
    }
}