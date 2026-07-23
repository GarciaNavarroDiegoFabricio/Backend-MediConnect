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
public class DocumentoClinicoResponseDTO {
    private Long idDocumento;
    private Long idHistoria;
    private Long idAtencion;
    private String nombreArchivo;
    private String urlArchivo;
    private String tipoDocumento;
    private LocalDateTime fechaCarga;
    private String usuarioCarga;
}