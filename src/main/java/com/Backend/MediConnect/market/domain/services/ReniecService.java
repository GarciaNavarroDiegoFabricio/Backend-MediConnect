package com.Backend.MediConnect.market.domain.services;

import com.Backend.MediConnect.market.domain.dto.ReniecResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReniecService {

    private static final String RENIEC_URL =
            "https://apiuser.aviva.pe/api/v1/patient/getFromRENIEC?documentNumber=";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ReniecService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public ReniecResponseDTO consultarDni(String dni) {
        ReniecResponseDTO resultado = new ReniecResponseDTO();

        try {
            String url = RENIEC_URL + dni;
            String rawJson = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(rawJson);

            boolean valid = root.path("valid").asBoolean(false);
            JsonNode data = root.path("data");

            if (!valid || data.isMissingNode() || data.isNull()
                    || !data.path("success").asBoolean(false)) {
                resultado.setEncontrado(false);
                resultado.setMensaje("DNI no encontrado en RENIEC. Por favor ingrese los datos manualmente.");
                return resultado;
            }

            String nombres         = data.path("nombres").asText("").trim();
            String apellidoPaterno = data.path("apellidoPaterno").asText("").trim();
            String apellidoMaterno = data.path("apellidoMaterno").asText("").trim();
            String fechaNacimiento = data.path("fechaNacimiento").asText("").trim();
            String ubigeo          = data.path("ubigeoSunat").asText("").trim();

            if (nombres.isEmpty() && apellidoPaterno.isEmpty()) {
                resultado.setEncontrado(false);
                resultado.setMensaje("RENIEC no retornó datos válidos. Por favor ingrese los datos manualmente.");
                return resultado;
            }

            resultado.setEncontrado(true);
            resultado.setMensaje("Datos obtenidos de RENIEC exitosamente.");
            resultado.setNumDocumento(data.path("numDocumento").asText(""));
            resultado.setNombres(nombres);
            resultado.setApellidoPaterno(apellidoPaterno);
            resultado.setApellidoMaterno(apellidoMaterno);
            resultado.setNombreCompleto(data.path("nombreCompleto").asText(""));
            resultado.setFechaNacimiento(fechaNacimiento);
            resultado.setSexo(data.path("sexo").asText(""));
            resultado.setEstadoCivil(data.path("estadoCivil").asText(""));
            resultado.setDepartamento(data.path("departamento").asText(""));
            resultado.setProvincia(data.path("provincia").asText(""));
            resultado.setDistrito(data.path("distrito").asText(""));
            resultado.setDireccionCompleta(data.path("direccionCompleta").asText(""));
            resultado.setUbigeo(ubigeo);

        } catch (Exception e) {
            resultado.setEncontrado(false);
            resultado.setMensaje("Error al consultar RENIEC. Por favor ingrese los datos manualmente.");
        }

        return resultado;
    }
}