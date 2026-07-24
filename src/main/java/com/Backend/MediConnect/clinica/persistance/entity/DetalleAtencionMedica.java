package com.Backend.MediConnect.clinica.persistance.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_atencion_medica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleAtencionMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consulta", nullable = false)
    private Consulta consulta;

    @Column(columnDefinition = "TEXT")
    private String tratamiento;

    @Column(name = "indicaciones_medicas", columnDefinition = "TEXT")
    private String indicacionesMedicas;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(columnDefinition = "TEXT")
    private String recomendaciones;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void prePersist() {
        fechaRegistro = LocalDateTime.now();
    }

}