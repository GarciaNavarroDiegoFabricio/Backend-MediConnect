package com.Backend.MediConnect.clinica.domain.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
public class HistorialClinicoDTO {

    private LocalDateTime fechaConsulta;
    private String medico;
    private String diagnostico;
    private String estadoConsulta;

    public HistorialClinicoDTO() {
    }

}
