package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloqueoHorarioResponseDTO {
    private Long idBloqueo;
    private Long idMedico;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String motivo;
    private String tipo;
}