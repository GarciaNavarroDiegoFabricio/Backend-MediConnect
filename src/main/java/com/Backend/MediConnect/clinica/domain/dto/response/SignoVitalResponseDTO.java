package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignoVitalResponseDTO {

    private Long idSigno;

    private String presionArterial;

    private Integer frecuenciaCardiaca;

    private Double temperatura;

    private Double peso;

    private Double talla;

}