package com.Backend.MediConnect.clinica.domain.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
public class HorarioResponseDTO {
    private Integer idHorario;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer intervaloMinutos;
    private String estado;
    private String nombreMedico;
    private String dniMedico;
}