package com.Backend.MediConnect.clinica.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SedePublicaResponseDTO {
    private Long idSede;
    private String nombre;
    private String descripcion;
    private String direccion;
    private String foto;
    private String estado;
}