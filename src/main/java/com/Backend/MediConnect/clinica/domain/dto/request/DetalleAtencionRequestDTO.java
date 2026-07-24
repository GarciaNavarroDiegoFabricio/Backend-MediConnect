package com.Backend.MediConnect.clinica.domain.dto.request;

import lombok.*;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleAtencionRequestDTO {

    @NotBlank(message = "El tratamiento es obligatorio.")
    private String tratamiento;

    @NotBlank(message = "Las indicaciones médicas son obligatorias.")
    private String indicacionesMedicas;

    private String observaciones;

    private String recomendaciones;
}