package com.Backend.MediConnect.market.domain.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter @Setter
public class ReporteResponseDTO {
    private Integer idReporte;
    private LocalTime fechaReporte;
    private Integer citasAtendidas;
    private Integer citasCanceladas;
    private Integer citasReprogramadas;
    private Integer citasPendientes;
}