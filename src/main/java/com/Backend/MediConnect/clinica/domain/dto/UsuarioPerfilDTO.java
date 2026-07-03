package com.Backend.MediConnect.clinica.domain.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioPerfilDTO {
    private Integer id;
    private String dni;
    private String rol;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String correo;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String numeroColegiatura;
    private List<String> especialidades;
    private String sede;
}