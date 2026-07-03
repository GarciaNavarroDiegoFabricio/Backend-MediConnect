package com.Backend.MediConnect.clinica.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistorialClinicoAgrupadoDTO {

    private LocalDate fecha;
    private List<AtencionMedicaDTO> atenciones;

}