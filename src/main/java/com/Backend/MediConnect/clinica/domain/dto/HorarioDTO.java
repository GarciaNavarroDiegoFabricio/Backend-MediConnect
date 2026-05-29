package com.Backend.MediConnect.clinica.domain.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
public class HorarioDTO {
    private Integer idMedico;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer intervaloMinutos;
}