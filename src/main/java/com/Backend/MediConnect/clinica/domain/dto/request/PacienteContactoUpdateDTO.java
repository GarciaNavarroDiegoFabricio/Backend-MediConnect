package com.Backend.MediConnect.clinica.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteContactoUpdateDTO {
    private String telefono;
    private String contactoEmergenciaNombre;
    private String contactoEmergenciaTelefono;
    private String contactoEmergenciaParentesco;
}