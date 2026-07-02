package com.Backend.MediConnect.clinica.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BloquearHorarioDTO {
    private String motivo; // Ejemplo: "VACACIONES", "EMERGENCIA_MEDICA", "MANTENIMIENTO"
}