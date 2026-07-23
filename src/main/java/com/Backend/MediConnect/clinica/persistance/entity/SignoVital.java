package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "signo_vital")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignoVital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_signo")
    private Long idSigno;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_atencion", referencedColumnName = "id_atencion", unique = true, nullable = false)
    private AtencionMedica atencionMedica;

    @Column(name = "presion_arterial", length = 20)
    private String presionArterial;

    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

    @Column(name = "frecuencia_respiratoria")
    private Integer frecuenciaRespiratoria;

    @Column(name = "temperatura")
    private Double temperatura;

    @Column(name = "saturacion_oxigeno")
    private Integer saturacionOxigeno;

    @Column(name = "peso")
    private Double peso;

    @Column(name = "talla")
    private Double talla;
}