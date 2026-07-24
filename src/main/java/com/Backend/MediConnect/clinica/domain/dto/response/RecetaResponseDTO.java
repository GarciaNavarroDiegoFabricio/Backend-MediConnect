package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecetaResponseDTO {

    private Long idReceta;

    private Long idConsulta;

    private String nombrePaciente;

    private String nombreMedico;

    private String prescripcion;

    private LocalDate fecha;

    private List<DetalleRecetaResponseDTO> detalles;

}