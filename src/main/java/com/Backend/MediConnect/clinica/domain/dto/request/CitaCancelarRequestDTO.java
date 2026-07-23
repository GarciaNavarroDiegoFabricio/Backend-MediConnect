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
public class CitaCancelarRequestDTO {

    @NotBlank(message = "El motivo de cancelación es obligatorio.")
    private String motivo;
}