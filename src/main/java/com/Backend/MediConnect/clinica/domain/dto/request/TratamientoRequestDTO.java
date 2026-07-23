package com.Backend.MediConnect.clinica.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TratamientoRequestDTO {

    @NotBlank(message = "Las indicaciones son obligatorias.")
    private String indicaciones;

    private String recomendaciones;
}