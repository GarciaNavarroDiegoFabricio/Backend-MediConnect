package com.Backend.MediConnect.clinica.domain.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecetaRequestDTO {

    private String observaciones;

    @NotEmpty(message = "Debe registrar al menos un medicamento en la receta.")
    @Valid
    private List<DetalleRecetaRequestDTO> detalles;
}