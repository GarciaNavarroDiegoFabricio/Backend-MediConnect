package com.Backend.MediConnect.clinica.domain.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinalizarConsultaRequestDTO {

    private SignoVitalRequestDTO signosVitales;

    private DiagnosticoMedicoRequestDTO diagnostico;

    private DetalleAtencionRequestDTO detalleAtencion;

}