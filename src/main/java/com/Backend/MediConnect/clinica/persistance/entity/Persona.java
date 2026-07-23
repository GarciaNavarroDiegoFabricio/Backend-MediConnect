package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long idPersona;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", unique = true)
    private Usuario usuario;

    @Column(name = "dni", nullable = false, unique = true, length = 8)
    private String dni;

    @Column(name = "codigo_verificacion", length = 20)
    private String codigoVerificacion;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellido_paterno", nullable = false, length = 100)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", length = 100)
    private String apellidoMaterno;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "sexo", length = 20)
    private String sexo;

    @Column(name = "estado_civil", length = 20)
    private String estadoCivil;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "ubigeo", length = 10)
    private String ubigeo;

    @Column(name = "departamento", length = 100)
    private String departamento;

    @Column(name = "provincia", length = 100)
    private String provincia;

    @Column(name = "distrito", length = 100)
    private String distrito;

    @Column(name = "foto_perfil", length = 500)
    private String fotoPerfil;

    @Column(name = "foto_perfil_public_id", length = 255)
    private String fotoPerfilPublicId;
}