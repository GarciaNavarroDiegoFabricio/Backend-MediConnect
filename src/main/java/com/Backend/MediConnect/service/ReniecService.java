package com.Backend.MediConnect.service;

import com.Backend.MediConnect.dto.PatientProfileResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ReniecService {

    private final RestTemplate restTemplate;

    private static final String RENIEC_URL = "https://apiuser.aviva.pe/api/v1/patient/getFromRENIEC?documentNumber=";

    public ReniecService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PatientProfileResponse getPatientByDni(String dni) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("origin", "https://miaviva.aviva.pe");
        headers.set("referer", "https://miaviva.aviva.pe/");
        headers.set("user-agent", "Mozilla/5.0");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                RENIEC_URL + dni,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        Map<String, Object> body = response.getBody();
        if (body == null) {
            throw new RuntimeException("Respuesta vacía");
        }

        Object dataObj = body.get("data");
        if (!(dataObj instanceof Map<?, ?> dataMap)) {
            throw new RuntimeException("Formato inválido");
        }

        Map<String, Object> data = dataMap.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        e -> String.valueOf(e.getKey()),
                        Map.Entry::getValue
                ));

        PatientProfileResponse profile = new PatientProfileResponse();
        profile.setNumDocumento((String) data.get("numDocumento"));
        profile.setNombreCompleto((String) data.get("nombreCompleto"));
        profile.setNombres((String) data.get("nombres"));
        profile.setApellidoPaterno((String) data.get("apellidoPaterno"));
        profile.setApellidoMaterno((String) data.get("apellidoMaterno"));
        profile.setFechaNacimiento((String) data.get("fechaNacimiento"));
        profile.setSexo((String) data.get("sexo"));
        profile.setEstadoCivil((String) data.get("estadoCivil"));
        profile.setDepartamento((String) data.get("departamento"));
        profile.setProvincia((String) data.get("provincia"));
        profile.setDistrito((String) data.get("distrito"));
        profile.setDireccionCompleta((String) data.get("direccionCompleta"));

        return profile;
    }
}