package com.Backend.MediConnect.clinica.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PacienteBusquedaDTO {
    private Integer idPaciente;
    private String dni;
    private String nombreCompleto;
    private Integer idHistoriaClinica;
    private String correo;
    private String telefono;
}