package com.Backend.MediConnect.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PatientProfileResponse {

    private String numDocumento;
    private String nombreCompleto;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String fechaNacimiento;
    private String sexo;
    private String estadoCivil;
    private String departamento;
    private String provincia;
    private String distrito;
    private String direccionCompleta;

}