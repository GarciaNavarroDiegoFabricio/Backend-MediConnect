package com.Backend.MediConnect.clinica.domain.services;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class VideollamadaService {

    public String generarEnlace() {
        // Genera un identificador único para evitar colisiones entre salas
        String salaId = "MediConnect-" + UUID.randomUUID().toString().substring(0, 8);
        return "https://meet.jit.si/" + salaId;
    }
}
