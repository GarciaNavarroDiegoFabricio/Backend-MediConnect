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
public class AtencionMedicaResponseDTO {
    private Long idAtencion;
    private Long idCita;
    private String nombrePaciente;
    private String nombreMedico;
    private String motivoConsulta;
    private String observaciones;
    private String estado;
    private LocalDateTime fechaAtencion;
    private LocalDateTime fechaCierre;
    private SignoVitalResponseDTO signoVital;
    private List<DiagnosticoResponseDTO> diagnosticos;
    private List<TratamientoResponseDTO> tratamientos;
}