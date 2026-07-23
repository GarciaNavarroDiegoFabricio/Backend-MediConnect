package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "atencion_medica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtencionMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atencion")
    private Long idAtencion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cita", referencedColumnName = "id_cita", unique = true, nullable = false)
    private Cita cita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_historia", nullable = false)
    private HistoriaClinica historiaClinica;

    @Column(name = "motivo_consulta", length = 500)
    private String motivoConsulta;

    @Column(name = "observaciones", length = 2000)
    private String observaciones;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "fecha_atencion", nullable = false)
    private LocalDateTime fechaAtencion;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @Column(name = "usuario_creacion", length = 100)
    private String usuarioCreacion;

    @PrePersist
    public void prePersist() {
        this.fechaAtencion = LocalDateTime.now();
        if (this.estado == null) this.estado = "EN_CURSO";
    }
}