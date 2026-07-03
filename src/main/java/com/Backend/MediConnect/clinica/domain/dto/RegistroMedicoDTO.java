package com.Backend.MediConnect.clinica.domain.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

import jakarta.persistence.Column;

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

    private String numeroColegiatura;

    private List<Integer> idEspecialidades;

    private Integer idSede;

    private Boolean disponible;

    private List<HorarioDTO> horarios;
}