package com.Backend.MediConnect.clinica.domain.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateRequestDTO {

    @Email(message = "El correo no tiene un formato válido")
    private String correo;

    private Long idSede;

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