package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroPacienteResponseDTO {
    private Long idUsuario;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
}