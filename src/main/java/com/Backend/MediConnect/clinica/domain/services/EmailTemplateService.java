package com.Backend.MediConnect.clinica.domain.services;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailTemplateService {

    public String cargarPlantilla(String nombreArchivo) {
        try (InputStream is = new ClassPathResource("templates/" + nombreArchivo).getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar la plantilla de correo: " + nombreArchivo, e);
        }
    }

    public String reemplazarPlaceholders(String html, Map<String, String> valores) {
        String resultado = html;
        for (Map.Entry<String, String> entry : valores.entrySet()) {
            resultado = resultado.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return resultado;
    }
}