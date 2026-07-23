package com.Backend.MediConnect.clinica.domain.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaReprogramarRequestDTO {

    @NotNull(message = "La nueva fecha es obligatoria.")
    @Future(message = "La nueva fecha debe ser futura.")
    private LocalDate nuevaFecha;

    @NotNull(message = "La nueva hora de inicio es obligatoria.")
    private LocalTime nuevaHoraInicio;

    @NotNull(message = "La nueva hora de fin es obligatoria.")
    private LocalTime nuevaHoraFin;

    private String motivo;
}