package com.Backend.MediConnect.service;

import com.Backend.MediConnect.dto.AuthResponse;
import com.Backend.MediConnect.dto.LoginRequest;
import com.Backend.MediConnect.dto.RegisterRequest;
import com.Backend.MediConnect.entity.Role;
import com.Backend.MediConnect.entity.User;
import com.Backend.MediConnect.repository.UserRepository;
import com.Backend.MediConnect.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email ya registrado");
        }
        if (userRepository.existsByDni(request.getDni())) {
            throw new RuntimeException("DNI ya registrado");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDni(request.getDni());
        user.setFullName(request.getFullName());
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getRole().name(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getRole().name(), user.getEmail());
    }
}