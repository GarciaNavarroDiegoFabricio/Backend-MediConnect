package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosticoMedicoResponseDTO {

    private Long idDiagnostico;
    private String descripcionClinica;
    private String categoriaDiagnostica;
    private LocalDateTime fechaRegistro;

}