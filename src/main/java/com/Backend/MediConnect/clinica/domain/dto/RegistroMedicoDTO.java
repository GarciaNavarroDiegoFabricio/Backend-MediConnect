package com.Backend.MediConnect.clinica.domain.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class RegistroMedicoDTO {
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String dni;
    private Integer edad;
    private String password;
    private List<Integer> idEspecialidades;
    private Integer idSede;
}