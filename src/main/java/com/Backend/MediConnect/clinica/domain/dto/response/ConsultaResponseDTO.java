package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaResponseDTO {

    private Long idConsulta;

    private Long idPaciente;

    private String nombrePaciente;

    private String nombreMedico;

    private String estado;

    private LocalDateTime horaInicio;

    private LocalDateTime horaFin;

    private SignoVitalResponseDTO signoVital;

    private List<DiagnosticoMedicoResponseDTO> diagnosticos;

    private DetalleAtencionResponseDTO detalleAtencion;

    private RecetaResponseDTO receta;
}