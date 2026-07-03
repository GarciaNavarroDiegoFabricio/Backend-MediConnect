package com.Backend.MediConnect.clinica.domain.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultaResponseDTO {

    private Integer idConsulta;

    private Integer idCita;

    private LocalDateTime horaInicio;

    private LocalDateTime horaFin;

    private String estado;

}