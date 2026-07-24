package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoClinicoResponseDTO {

    private Long idDocumento;

    private Long idConsulta;

    private String nombreArchivo;

    private String tipoDocumento;

    private String rutaArchivo;

    private LocalDateTime fechaSubida;
}