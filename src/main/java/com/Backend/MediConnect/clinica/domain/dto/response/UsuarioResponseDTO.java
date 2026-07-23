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
public class UsuarioResponseDTO {

    private Long idUsuario;
    private String correo;
    private Integer idRol;
    private String nombreRol;
    private Long idSede;
    private String estado;
    private Integer intentosFallidos;
    private LocalDateTime fechaBloqueo;

    private String dni;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String fechaNacimiento;
    private String sexo;
    private String estadoCivil;
    private String direccion;
    private String departamento;
    private String provincia;
    private String distrito;
    private String fotoPerfil;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}