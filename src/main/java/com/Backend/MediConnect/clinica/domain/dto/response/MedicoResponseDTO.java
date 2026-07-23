package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoResponseDTO {
    private Long idMedico;
    private Long idUsuario;
    private String dni;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correo;
    private String fotoPerfil;
    private String numeroColegiatura;
    private Long idEspecialidad;
    private String nombreEspecialidad;
    private Long idSede;
    private String nombreSede;
    private Boolean disponible;
    private String estado;
}