package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tratamiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tratamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tratamiento")
    private Long idTratamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_atencion", nullable = false)
    private AtencionMedica atencionMedica;

    @Column(name = "indicaciones", nullable = false, length = 2000)
    private String indicaciones;

    @Column(name = "recomendaciones", length = 1000)
    private String recomendaciones;
}