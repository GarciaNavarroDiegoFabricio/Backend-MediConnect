package com.Backend.MediConnect.clinica.domain.dto.request;

import lombok.*;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosticoMedicoRequestDTO {

    @NotBlank(message = "La descripción clínica es obligatoria.")
    private String descripcionClinica;

    @NotBlank(message = "La categoría diagnóstica es obligatoria.")
    private String categoriaDiagnostica;
}