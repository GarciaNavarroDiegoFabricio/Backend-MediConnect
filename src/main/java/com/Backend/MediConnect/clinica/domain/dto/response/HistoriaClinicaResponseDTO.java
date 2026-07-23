package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriaClinicaResponseDTO {
    private Long idHistoria;
    private Long idPaciente;
    private String nombrePaciente;
    private LocalDateTime fechaCreacion;
    private List<AntecedenteClinicoResponseDTO> antecedentes;
    private List<AtencionMedicaResponseDTO> atenciones;
    private List<DocumentoClinicoResponseDTO> documentos;
}