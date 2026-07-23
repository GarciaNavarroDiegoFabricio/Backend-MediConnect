package com.Backend.MediConnect.clinica.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilUpdateRequestDTO {
    private String direccion;
    private String estadoCivil;
    private String correo;
}