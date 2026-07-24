package com.Backend.MediConnect.clinica.domain.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignoVitalRequestDTO {

    @NotBlank(message = "La presión arterial es obligatoria.")
    private String presionArterial;

    @NotNull(message = "La frecuencia cardíaca es obligatoria.")
    @Positive(message = "La frecuencia cardíaca debe ser mayor que 0.")
    private Integer frecuenciaCardiaca;

    @NotNull(message = "La frecuencia respiratoria es obligatoria.")
    @Positive(message = "La frecuencia respiratoria debe ser mayor que 0.")
    private Integer frecuenciaRespiratoria;

    @NotNull(message = "La temperatura es obligatoria.")
    @Positive(message = "La temperatura debe ser mayor que 0.")
    private Double temperatura;

    @NotNull(message = "La saturación de oxígeno es obligatoria.")
    @Min(value = 0)
    @Max(value = 100)
    private Integer saturacionOxigeno;

    @NotNull(message = "El peso es obligatorio.")
    @Positive(message = "El peso debe ser mayor que 0.")
    private Double peso;

    @NotNull(message = "La talla es obligatoria.")
    @Positive(message = "La talla debe ser mayor que 0.")
    private Double talla;
}