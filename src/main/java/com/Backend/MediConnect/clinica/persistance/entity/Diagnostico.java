package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diagnostico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_diagnostico")
    private Long idDiagnostico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_atencion", nullable = false)
    private AtencionMedica atencionMedica;

    @Column(name = "codigo_cie10", length = 10)
    private String codigoCie10;

    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;
}