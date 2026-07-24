package com.Backend.MediConnect.clinica.domain.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AntecedentePacienteRequestDTO {

    @Size(max = 2000)
    private String antecedentesPersonales;

    @Size(max = 2000)
    private String antecedentesFamiliares;

    @Size(max = 2000)
    private String alergias;

    @Size(max = 2000)
    private String condicionesRelevantes;

}