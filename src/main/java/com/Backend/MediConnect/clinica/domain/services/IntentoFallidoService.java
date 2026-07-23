package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.repository.IUsuarioRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class IntentoFallidoService {

    private static final int MAX_INTENTOS = 5;

    private final IUsuarioRepository usuarioRepository;
    private final EmailService emailService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public IntentoFallidoService(IUsuarioRepository usuarioRepository, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrarIntentoFallido(Long idUsuario, String nombres, String apellidoPaterno, String apellidoMaterno) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        int intentos = usuario.getIntentosFallidos() + 1;
        usuario.setIntentosFallidos(intentos);

        if (intentos >= MAX_INTENTOS) {
            usuario.setEstado("BLOQUEADO");
            usuario.setFechaBloqueo(LocalDateTime.now());

            String token = UUID.randomUUID().toString();
            usuario.setTokenReset(token);
            usuario.setTokenResetExpira(LocalDateTime.now().plusHours(1));

            String nombreCompleto = String.join(" ",
                    safe(nombres), safe(apellidoPaterno), safe(apellidoMaterno)).trim();

            String enlace = frontendUrl + "portal-web?token=" + token;
            emailService.enviarCorreoBloqueo(usuario.getCorreo(), nombreCompleto, enlace);
        }

        usuarioRepository.save(usuario);
    }

    private String safe(String valor) {
        return valor != null ? valor : "";
    }
}