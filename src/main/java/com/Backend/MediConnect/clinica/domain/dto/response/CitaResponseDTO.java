package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaResponseDTO {
    private Long idCita;
    private Long idPaciente;
    private String nombrePaciente;
    private Long idMedico;
    private String nombreMedico;
    private String nombreEspecialidad;
    private LocalDate fechaCita;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String modalidad;
    private String enlaceVideollamada;
    private String motivoConsulta;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}