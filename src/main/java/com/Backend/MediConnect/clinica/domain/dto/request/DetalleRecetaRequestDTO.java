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
public class DetalleRecetaRequestDTO {

    @NotBlank(message = "El medicamento es obligatorio.")
    private String medicamento;

    @NotBlank(message = "La dosis es obligatoria.")
    private String dosis;

    @NotBlank(message = "La frecuencia es obligatoria.")
    private String frecuencia;

    private String duracion;

    private String indicaciones;
}