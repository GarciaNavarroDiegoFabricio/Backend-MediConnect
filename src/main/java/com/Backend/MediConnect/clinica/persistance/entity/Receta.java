package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "receta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_receta")
    private Long idReceta;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_atencion", referencedColumnName = "id_atencion", unique = true, nullable = false)
    private AtencionMedica atencionMedica;

    @Column(name = "codigo_receta", nullable = false, unique = true, length = 20)
    private String codigoReceta;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @PrePersist
    public void prePersist() {
        this.fechaEmision = LocalDateTime.now();
    }
}