package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpedienteClinicoResponseDTO {

    private Long idExpediente;

    private Long idPaciente;

    private String nombrePaciente;

    private LocalDateTime fechaCreacion;

    private String estado;

    private AntecedentePacienteResponseDTO antecedentes;

    private List<ConsultaResponseDTO> consultas;

}