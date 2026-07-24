package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignoVitalRequestDTO {

    private BigDecimal peso;

    private BigDecimal talla;

    private String presionArterial;

    private BigDecimal temperatura;

    private Integer frecuenciaCardiaca;

}