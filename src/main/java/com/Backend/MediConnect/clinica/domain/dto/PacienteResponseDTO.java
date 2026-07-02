package com.Backend.MediConnect.clinica.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PacienteResponseDTO {
    private Integer idPaciente;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String dni;
    private String correo;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String ubigeo;
    private String codigoHistoriaClinica;
}