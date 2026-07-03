package com.Backend.MediConnect.clinica.domain.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtencionMedicaDTO {

    private String medico;
    private LocalDateTime horaConsulta;
    private String estadoConsulta;
    private String diagnostico;
}
