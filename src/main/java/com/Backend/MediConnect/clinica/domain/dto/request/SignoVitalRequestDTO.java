package com.Backend.MediConnect.clinica.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignoVitalRequestDTO {
    private String presionArterial;
    private Integer frecuenciaCardiaca;
    private Integer frecuenciaRespiratoria;
    private Double temperatura;
    private Integer saturacionOxigeno;
    private Double peso;
    private Double talla;
}