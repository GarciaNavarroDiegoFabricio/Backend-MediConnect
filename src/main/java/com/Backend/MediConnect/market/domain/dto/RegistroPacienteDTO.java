package com.Backend.MediConnect.market.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class RegistroPacienteDTO {
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String dni;
    private String correo;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String ubigeo;
    private String password;

}