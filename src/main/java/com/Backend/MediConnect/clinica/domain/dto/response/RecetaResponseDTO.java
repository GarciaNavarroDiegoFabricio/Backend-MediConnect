package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecetaResponseDTO {
    private Long idReceta;
    private Long idAtencion;
    private String codigoReceta;
    private String nombrePaciente;
    private String nombreMedico;
    private String especialidad;
    private String observaciones;
    private LocalDateTime fechaEmision;
    private List<DetalleRecetaResponseDTO> detalles;
}