package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialCitaResponseDTO {
    private Long idHistorial;
    private String estadoAnterior;
    private String estadoNuevo;
    private String motivoCambio;
    private LocalDateTime fechaCambio;
    private String usuarioCambio;
}