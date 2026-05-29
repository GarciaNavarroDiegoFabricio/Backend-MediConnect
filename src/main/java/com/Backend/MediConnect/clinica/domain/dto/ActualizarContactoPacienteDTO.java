package com.Backend.MediConnect.clinica.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarContactoPacienteDTO {
    private String correo;
    private String telefono;
    private String ubigeo;
}