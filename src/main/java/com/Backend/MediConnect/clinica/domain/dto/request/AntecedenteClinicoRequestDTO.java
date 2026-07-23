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
public class AntecedenteClinicoRequestDTO {

    @NotBlank(message = "El tipo de antecedente es obligatorio.")
    private String tipo;

    @NotBlank(message = "La descripción es obligatoria.")
    private String descripcion;
}