package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AntecedentePacienteResponseDTO {

    private Long idAntecedente;

    private Long idExpediente;

    private String antecedentesPersonales;

    private String antecedentesFamiliares;

    private String alergias;

    private String condicionesRelevantes;

    private LocalDateTime fechaActualizacion;

    private String usuarioModificacion;

}