package com.Backend.MediConnect.market.web.controller;

import com.Backend.MediConnect.market.domain.dto.*;
import com.Backend.MediConnect.market.domain.interfaces.IUsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IUsuarioService usuarioService;

    public AuthController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

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
    public ResponseEntity<AuthResponse> registrarMedico(@RequestBody RegistroMedicoDTO dto,
                                                        Authentication auth) {
        String dni = auth.getName();
        String rol = auth.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok(usuarioService.registrarMedico(dto, dni, rol));
    }
}