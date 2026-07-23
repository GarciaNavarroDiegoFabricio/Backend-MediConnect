package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosticoResponseDTO {
    private Long idDiagnostico;
    private String codigoCie10;
    private String descripcion;
    private String tipo;
}