package com.Backend.MediConnect.clinica.domain.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class MedicoResponseDTO {
    private Integer idMedico;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String dni;
    private Integer edad;
    private Boolean disponible;
    private List<String> especialidades;
    private List<String> sedes;
}