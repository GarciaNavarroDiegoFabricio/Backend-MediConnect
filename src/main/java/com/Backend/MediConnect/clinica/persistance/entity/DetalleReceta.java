package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_receta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleReceta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_receta")
    private Long idDetalleReceta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_receta", nullable = false)
    private Receta receta;

    @Column(name = "medicamento", nullable = false, length = 200)
    private String medicamento;

    @Column(name = "dosis", nullable = false, length = 100)
    private String dosis;

    @Column(name = "frecuencia", nullable = false, length = 100)
    private String frecuencia;

    @Column(name = "duracion", length = 100)
    private String duracion;

    @Column(name = "indicaciones", length = 500)
    private String indicaciones;
}