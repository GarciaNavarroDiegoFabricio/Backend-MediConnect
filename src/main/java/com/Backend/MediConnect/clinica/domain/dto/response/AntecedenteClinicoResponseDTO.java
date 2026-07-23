package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AntecedenteClinicoResponseDTO {
    private Long idAntecedente;
    private String tipo;
    private String descripcion;
    private LocalDateTime fechaRegistro;
    private String usuarioRegistro;
}