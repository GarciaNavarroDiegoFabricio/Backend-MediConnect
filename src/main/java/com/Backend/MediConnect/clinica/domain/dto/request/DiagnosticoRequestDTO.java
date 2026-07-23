package com.Backend.MediConnect.clinica.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosticoRequestDTO {

    private String codigoCie10;

    @NotBlank(message = "La descripción del diagnóstico es obligatoria.")
    private String descripcion;

    @NotBlank(message = "El tipo de diagnóstico es obligatorio.")
    private String tipo;
}