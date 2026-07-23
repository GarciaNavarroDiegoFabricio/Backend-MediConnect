package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.response.ReniecResponseDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.IReniecService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReniecService implements IReniecService {

    @Value("${reniec.api.url}")
    private String reniecUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ReniecService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public ReniecResponseDTO consultarDni(String dni) {
        ReniecResponseDTO resultado = new ReniecResponseDTO();

        try {
            String rawJson = restTemplate.getForObject(reniecUrl + dni, String.class);
            JsonNode root = objectMapper.readTree(rawJson);

            if (!root.path("valid").asBoolean(false)) {
                resultado.setEncontrado(false);
                resultado.setMensaje("DNI no encontrado en RENIEC. Por favor ingrese los datos manualmente.");
                return resultado;
            }

            JsonNode data = root.path("data");

            if (data.isMissingNode() || data.isNull() || !data.path("success").asBoolean(false)) {
                resultado.setEncontrado(false);
                resultado.setMensaje("No se encontraron datos válidos en RENIEC.");
                return resultado;
            }

            resultado.setEncontrado(true);
            resultado.setMensaje(root.path("message").asText("Datos obtenidos de RENIEC."));
            resultado.setNumDocumento(data.path("numDocumento").asText(""));
            resultado.setCodigoVerificacion(data.path("codigoVerificacion").asText(""));
            resultado.setNombres(data.path("nombres").asText(""));
            resultado.setApellidoPaterno(data.path("apellidoPaterno").asText(""));
            resultado.setApellidoMaterno(data.path("apellidoMaterno").asText(""));
            resultado.setFechaNacimiento(data.path("fechaNacimiento").asText(""));
            resultado.setSexo(data.path("sexo").asText(""));
            resultado.setEstadoCivil(data.path("estadoCivil").asText(""));
            resultado.setDireccion(data.path("direccion").asText(""));
            resultado.setUbigeo(data.path("ubigeoReniec").asText(""));
            resultado.setDepartamento(data.path("departamento").asText(""));
            resultado.setProvincia(data.path("provincia").asText(""));
            resultado.setDistrito(data.path("distrito").asText(""));

        } catch (Exception e) {
            resultado.setEncontrado(false);
            resultado.setMensaje("Error al consultar RENIEC. Por favor ingrese los datos manualmente.");
        }

        return resultado;
    }
}