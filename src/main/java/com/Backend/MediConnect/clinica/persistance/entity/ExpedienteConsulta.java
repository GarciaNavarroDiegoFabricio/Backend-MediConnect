package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "expediente_consulta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpedienteConsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_expediente_consulta")
    private Long idExpedienteConsulta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_expediente", nullable = false)
    private ExpedienteClinico expediente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consulta", nullable = false)
    private Consulta consulta;

}