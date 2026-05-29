package com.Backend.MediConnect.clinica.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReniecResponseDTO {
    private String numDocumento;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombreCompleto;
    private String fechaNacimiento;
    private String sexo;
    private String estadoCivil;
    private String departamento;
    private String provincia;
    private String distrito;
    private String direccionCompleta;
    private String ubigeo;
    private boolean encontrado;
    private String mensaje;
}