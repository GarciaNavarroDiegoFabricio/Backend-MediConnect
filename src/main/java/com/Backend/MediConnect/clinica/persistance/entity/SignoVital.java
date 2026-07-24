package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consulta", nullable = false)
    private Consulta consulta;

    @Column(name = "peso")
    private BigDecimal peso;

    @Column(name = "talla")
    private BigDecimal talla;

    @Column(name = "presion_arterial")
    private String presionArterial;

    @Column(name = "temperatura")
    private BigDecimal temperatura;

    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void prePersist() {

        this.fechaRegistro = LocalDateTime.now();

    }

    @Column(name = "frecuencia_respiratoria")
    private Integer frecuenciaRespiratoria;

    @Column(name = "saturacion_oxigeno")
    private Integer saturacionOxigeno;

}