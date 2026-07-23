package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.persistance.entity.Cita;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    @Value("${resend.api.url}")
    private String resendUrl;

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${resend.from}")
    private String remitente;

    private final RestTemplate restTemplate;
    private final EmailTemplateService emailTemplateService;

    public EmailService(RestTemplate restTemplate, EmailTemplateService emailTemplateService) {
        this.restTemplate = restTemplate;
        this.emailTemplateService = emailTemplateService;
    }

    public void enviarCorreoBloqueo(String correoDestino, String nombreCompleto, String enlaceRestablecer) {
        String plantilla = emailTemplateService.cargarPlantilla("email-bloqueo.html");

        Map<String, String> valores = new HashMap<>();
        valores.put("NOMBRE_COMPLETO", nombreCompleto);
        valores.put("ENLACE_RESET", enlaceRestablecer);

        String html = emailTemplateService.reemplazarPlaceholders(plantilla, valores);
        enviar(correoDestino, "Cuenta bloqueada - Restablece tu contraseña", html);
    }

    public void enviarCorreoBienvenida(String correoDestino, String nombreCompleto, String nombreRol, String descripcionRol) {
        String plantilla = emailTemplateService.cargarPlantilla("email-bienvenida.html");

        Map<String, String> valores = new HashMap<>();
        valores.put("NOMBRE_COMPLETO", nombreCompleto);
        valores.put("ROL", nombreRol);
        valores.put("DESCRIPCION_ROL", descripcionRol);

        String html = emailTemplateService.reemplazarPlaceholders(plantilla, valores);
        enviar(correoDestino, "Bienvenido/a a MediConnect", html);
    }

    public void enviarConfirmacionCita(String correoDestino, String nombrePaciente, Cita cita) {
        String plantilla = emailTemplateService.cargarPlantilla("email-confirmacion-cita.html");

        Map<String, String> valores = new HashMap<>();
        valores.put("NOMBRE_COMPLETO", nombrePaciente);
        valores.put("NOMBRE_MEDICO", construirNombreMedico(cita));
        valores.put("ESPECIALIDAD", cita.getMedico().getEspecialidad().getNombre());
        valores.put("FECHA_CITA", cita.getFechaCita().format(FORMATO_FECHA));
        valores.put("HORA_CITA", cita.getHoraInicio().format(FORMATO_HORA) + " - " + cita.getHoraFin().format(FORMATO_HORA));
        valores.put("MODALIDAD", cita.getModalidad());
        valores.put("ENLACE_VIDEOLLAMADA", construirBloqueEnlace(cita));

        String html = emailTemplateService.reemplazarPlaceholders(plantilla, valores);
        enviar(correoDestino, "Cita confirmada - MediConnect", html);
    }

    public void enviarRecordatorioCita(String correoDestino, String nombrePaciente, Cita cita) {
        String plantilla = emailTemplateService.cargarPlantilla("email-recordatorio-cita.html");

        Map<String, String> valores = new HashMap<>();
        valores.put("NOMBRE_COMPLETO", nombrePaciente);
        valores.put("NOMBRE_MEDICO", construirNombreMedico(cita));
        valores.put("ESPECIALIDAD", cita.getMedico().getEspecialidad().getNombre());
        valores.put("FECHA_CITA", cita.getFechaCita().format(FORMATO_FECHA));
        valores.put("HORA_CITA", cita.getHoraInicio().format(FORMATO_HORA) + " - " + cita.getHoraFin().format(FORMATO_HORA));
        valores.put("MODALIDAD", cita.getModalidad());
        valores.put("ENLACE_VIDEOLLAMADA", construirBloqueEnlace(cita));

        String html = emailTemplateService.reemplazarPlaceholders(plantilla, valores);
        enviar(correoDestino, "Recordatorio de cita - MediConnect", html);
    }

    public void enviarCancelacionCita(String correoDestino, String nombrePaciente, Cita cita, String motivo) {
        String plantilla = emailTemplateService.cargarPlantilla("email-cancelacion-cita.html");

        Map<String, String> valores = new HashMap<>();
        valores.put("NOMBRE_COMPLETO", nombrePaciente);
        valores.put("NOMBRE_MEDICO", construirNombreMedico(cita));
        valores.put("ESPECIALIDAD", cita.getMedico().getEspecialidad().getNombre());
        valores.put("FECHA_CITA", cita.getFechaCita().format(FORMATO_FECHA));
        valores.put("HORA_CITA", cita.getHoraInicio().format(FORMATO_HORA) + " - " + cita.getHoraFin().format(FORMATO_HORA));
        valores.put("MOTIVO", motivo != null ? motivo : "No especificado");

        String html = emailTemplateService.reemplazarPlaceholders(plantilla, valores);
        enviar(correoDestino, "Cita cancelada - MediConnect", html);
    }

    public void enviarReprogramacionCita(String correoDestino, String nombrePaciente, Cita cita) {
        String plantilla = emailTemplateService.cargarPlantilla("email-reprogramacion-cita.html");

        Map<String, String> valores = new HashMap<>();
        valores.put("NOMBRE_COMPLETO", nombrePaciente);
        valores.put("NOMBRE_MEDICO", construirNombreMedico(cita));
        valores.put("ESPECIALIDAD", cita.getMedico().getEspecialidad().getNombre());
        valores.put("FECHA_CITA", cita.getFechaCita().format(FORMATO_FECHA));
        valores.put("HORA_CITA", cita.getHoraInicio().format(FORMATO_HORA) + " - " + cita.getHoraFin().format(FORMATO_HORA));
        valores.put("MODALIDAD", cita.getModalidad());
        valores.put("ENLACE_VIDEOLLAMADA", construirBloqueEnlace(cita));

        String html = emailTemplateService.reemplazarPlaceholders(plantilla, valores);
        enviar(correoDestino, "Cita reprogramada - MediConnect", html);
    }

    public void enviarReceta(String correoDestino, String nombrePaciente, String codigoReceta, byte[] pdfBytes) {
        String plantilla = emailTemplateService.cargarPlantilla("email-receta.html");

        Map<String, String> valores = new HashMap<>();
        valores.put("NOMBRE_COMPLETO", nombrePaciente);
        valores.put("CODIGO_RECETA", codigoReceta);

        String html = emailTemplateService.reemplazarPlaceholders(plantilla, valores);
        enviarConAdjunto(correoDestino, "Tu receta médica - MediConnect", html, pdfBytes,
                "receta_" + codigoReceta + ".pdf");
    }

    public void enviarConstanciaAtencion(String correoDestino, String nombrePaciente, byte[] pdfBytes) {
        String plantilla = emailTemplateService.cargarPlantilla("email-constancia-atencion.html");

        Map<String, String> valores = new HashMap<>();
        valores.put("NOMBRE_COMPLETO", nombrePaciente);

        String html = emailTemplateService.reemplazarPlaceholders(plantilla, valores);
        enviarConAdjunto(correoDestino, "Constancia de atención médica - MediConnect", html, pdfBytes,
                "constancia_atencion.pdf");
    }

    private String construirNombreMedico(Cita cita) {
        return String.join(" ",
                safe(cita.getMedico().getPersona().getNombres()),
                safe(cita.getMedico().getPersona().getApellidoPaterno()),
                safe(cita.getMedico().getPersona().getApellidoMaterno())).trim();
    }

    private String construirBloqueEnlace(Cita cita) {
        if (!"VIRTUAL".equalsIgnoreCase(cita.getModalidad()) || cita.getEnlaceVideollamada() == null) {
            return "";
        }
        return "<p style=\"color:#374151; font-size:14px; line-height:22px;\">" +
                "Enlace de videollamada: <a href=\"" + cita.getEnlaceVideollamada() + "\" style=\"color:#2563eb;\">" +
                cita.getEnlaceVideollamada() + "</a></p>";
    }

    private String safe(String valor) {
        return valor != null ? valor : "";
    }

    private void enviar(String destino, String asunto, String html) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(resendApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("from", remitente);
        body.put("to", destino);
        body.put("subject", asunto);
        body.put("html", html);

        try {
            restTemplate.postForEntity(resendUrl, new HttpEntity<>(body, headers), String.class);
            log.info("Correo enviado correctamente a {}", destino);
        } catch (Exception e) {
            log.error("Error al enviar correo a {}: {}", destino, e.getMessage());
        }
    }

    private void enviarConAdjunto(String destino, String asunto, String html, byte[] archivo, String nombreArchivo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(resendApiKey);

        Map<String, Object> adjunto = new HashMap<>();
        adjunto.put("filename", nombreArchivo);
        adjunto.put("content", Base64.getEncoder().encodeToString(archivo));

        Map<String, Object> body = new HashMap<>();
        body.put("from", remitente);
        body.put("to", destino);
        body.put("subject", asunto);
        body.put("html", html);
        body.put("attachments", List.of(adjunto));

        try {
            restTemplate.postForEntity(resendUrl, new HttpEntity<>(body, headers), String.class);
            log.info("Correo con adjunto enviado correctamente a {}", destino);
        } catch (Exception e) {
            log.error("Error al enviar correo con adjunto a {}: {}", destino, e.getMessage());
        }
    }
}