package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleRecetaResponseDTO {
    private Long idDetalleReceta;
    private String medicamento;
    private String dosis;
    private String frecuencia;
    private String duracion;
    private String indicaciones;
}