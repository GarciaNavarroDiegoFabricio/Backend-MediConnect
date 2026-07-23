package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioMedicoResponseDTO {
    private Long idHorario;
    private Long idMedico;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;
}