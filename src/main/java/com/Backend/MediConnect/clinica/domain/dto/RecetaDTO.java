package com.Backend.MediConnect.clinica.domain.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class RecetaDTO {
    private Integer idPaciente;
    private Integer idConsulta;
    private String prescripcion;
    private LocalDate fecha;
}