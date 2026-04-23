package com.Backend.MediConnect.market.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistroMedicoDTO {
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String dni;
    private Integer edad;
    private String password;

}