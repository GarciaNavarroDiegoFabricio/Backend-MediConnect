package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaInicioResponseDTO {

    private Long idConsulta;

    private Long idCita;

    private Long idPaciente;

    private String nombrePaciente;

    private Long idMedico;

    private String estado;

    private LocalDateTime horaInicio;

}