package com.Backend.MediConnect.clinica.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistroAdminLocalDTO {
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String dni;
    private Integer idSede;
    private String password;

}