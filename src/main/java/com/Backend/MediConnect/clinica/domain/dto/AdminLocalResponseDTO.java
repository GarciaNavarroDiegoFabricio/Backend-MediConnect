package com.Backend.MediConnect.clinica.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLocalResponseDTO {
    private Integer idAdminLocal;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String dni;
    private String nombreSede;
    private String estado;     // 👈 AGREGADO: Para activar/desactivar/suspender
    private Integer idSede;    // 👈 AGREGADO: Para el ID de la sede en el CRUD
}