package com.Backend.MediConnect.clinica.domain.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class RecetaResponseDTO {
    private Integer idReceta;
    private String prescripcion;
    private LocalDate fecha;
    private String nombreMedico;
    private String nombrePaciente;
    private Integer idConsulta;
}