package com.Backend.MediConnect.market.domain.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter
public class CitaDTO {
    private Integer idMedico;
    private LocalDate fecha;
    private LocalTime hora;
    private String especialidad;
    private String tipo;
    private Integer prioridad;
    private Integer idSede;
    private Integer duracionEstimada;
}