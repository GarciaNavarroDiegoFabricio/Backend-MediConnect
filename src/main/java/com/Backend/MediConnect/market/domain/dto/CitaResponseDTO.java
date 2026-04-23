package com.Backend.MediConnect.market.domain.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter
public class CitaResponseDTO {
    private Integer idCita;
    private LocalDate fecha;
    private LocalTime hora;
    private String especialidad;
    private String tipo;
    private Integer prioridad;
    private Integer duracionEstimada;
    private String estado;
    private String nombreMedico;
    private String dniMedico;
    private String nombrePaciente;
    private String dniPaciente;
    private String nombreSede;
}