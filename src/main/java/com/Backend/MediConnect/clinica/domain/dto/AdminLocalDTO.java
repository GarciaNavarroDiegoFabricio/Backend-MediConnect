package com.Backend.MediConnect.clinica.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLocalDTO {

    private String primerNombre;
    private String segundoNombre;

    private String primerApellido;
    private String segundoApellido;

    private String dni;

    private String password;

    private Integer idSede;
}