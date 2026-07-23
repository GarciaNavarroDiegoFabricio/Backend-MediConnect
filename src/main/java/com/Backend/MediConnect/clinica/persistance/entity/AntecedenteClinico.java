package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "antecedente_clinico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AntecedenteClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_antecedente")
    private Long idAntecedente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_historia", nullable = false)
    private HistoriaClinica historiaClinica;

    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;

    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "usuario_registro", length = 100)
    private String usuarioRegistro;

    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDateTime.now();
    }
}