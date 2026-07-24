package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleAtencionResponseDTO {

    private Long idDetalle;

    private String tratamiento;

    private String indicacionesMedicas;

    private String observaciones;

    private String recomendaciones;

    private LocalDateTime fechaRegistro;
}