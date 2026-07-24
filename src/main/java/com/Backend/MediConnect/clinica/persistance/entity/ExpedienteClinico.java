package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "expediente_clinico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpedienteClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_expediente")
    private Long idExpediente;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false, unique = true)
    private Paciente paciente;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "estado")
    private String estado;

    @Column(name = "usuario_creacion")
    private String usuarioCreacion;

    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();

        if (this.estado == null) {
            this.estado = "ACTIVO";
        }
    }
}