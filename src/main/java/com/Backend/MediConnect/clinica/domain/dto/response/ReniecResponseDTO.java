package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReniecResponseDTO {

    private boolean encontrado;
    private String mensaje;

    private String numDocumento;
    private String codigoVerificacion;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String fechaNacimiento;
    private String sexo;
    private String estadoCivil;
    private String direccion;
    private String ubigeo;
    private String departamento;
    private String provincia;
    private String distrito;
}