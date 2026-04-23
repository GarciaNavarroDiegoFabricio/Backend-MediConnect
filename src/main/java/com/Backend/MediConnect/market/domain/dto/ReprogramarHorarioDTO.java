package com.Backend.MediConnect.market.domain.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter @Setter
public class ReprogramarHorarioDTO {
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer intervaloMinutos;
}