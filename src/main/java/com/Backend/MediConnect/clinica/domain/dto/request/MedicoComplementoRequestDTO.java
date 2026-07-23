package com.Backend.MediConnect.clinica.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoComplementoRequestDTO {

    @NotBlank(message = "El número de colegiatura es obligatorio.")
    @Size(max = 20)
    private String numeroColegiatura;

    @NotNull(message = "La especialidad es obligatoria.")
    private Long idEspecialidad;
}