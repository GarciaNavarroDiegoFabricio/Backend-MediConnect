package com.Backend.MediConnect.clinica.web.security;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService {

    private final Map<String, Integer> intentosFallidos = new HashMap<>();
    private final Map<String, LocalDateTime> bloqueados = new HashMap<>();

    private static final int MAX_INTENTOS = 5;
    private static final int MINUTOS_BLOQUEO = 5;

    public boolean estaBloqueado(String dni) {

        if (!bloqueados.containsKey(dni)) {
            return false;
        }

        LocalDateTime tiempoFin = bloqueados.get(dni);

        if (LocalDateTime.now().isAfter(tiempoFin)) {
            bloqueados.remove(dni);
            intentosFallidos.remove(dni);
            return false;
        }

        return true;
    }

    public void registrarIntentoFallido(String dni) {

        int intentos = intentosFallidos.getOrDefault(dni, 0) + 1;

        intentosFallidos.put(dni, intentos);

        if (intentos >= MAX_INTENTOS) {

            bloqueados.put(
                    dni,
                    LocalDateTime.now().plusMinutes(MINUTOS_BLOQUEO));
        }
    }

    public void loginExitoso(String dni) {

        intentosFallidos.remove(dni);
        bloqueados.remove(dni);
    }
}