package com.Backend.MediConnect.clinica.domain.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
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
public class CitaRequestDTO {

    @NotNull(message = "El médico es obligatorio.")
    private Long idMedico;

    @NotNull(message = "La fecha de la cita es obligatoria.")
    @Future(message = "La fecha de la cita debe ser futura.")
    private LocalDate fechaCita;

    @NotNull(message = "La hora de inicio es obligatoria.")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria.")
    private LocalTime horaFin;

    @NotBlank(message = "La modalidad es obligatoria.")
    private String modalidad;

    private String motivoConsulta;

    @NotBlank(message = "El identificador del pago es obligatorio.")
    private String idPago;
}