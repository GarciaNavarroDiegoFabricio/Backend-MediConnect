package com.Backend.MediConnect.clinica.domain.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

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
    private String estado; // 👈 AGREGADO: Para activar/desactivar/suspender
    private List<String> especialidades;
    private List<String> sedes;
}