package com.Backend.MediConnect.clinica.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloqueoHorarioRequestDTO {

    @NotNull(message = "La fecha de inicio es obligatoria.")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria.")
    private LocalDate fechaFin;

    @NotBlank(message = "El motivo es obligatorio.")
    private String motivo;

    @NotBlank(message = "El tipo es obligatorio.")
    private String tipo;
}