package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "antecedente_paciente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AntecedentePaciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_antecedente")
    private Long idAntecedente;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_expediente", nullable = false, unique = true)
    private ExpedienteClinico expediente;

    @Column(name = "antecedentes_personales")
    private String antecedentesPersonales;

    @Column(name = "antecedentes_familiares")
    private String antecedentesFamiliares;

    @Column(name = "alergias")
    private String alergias;

    @Column(name = "condiciones_relevantes")
    private String condicionesRelevantes;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;

    @PrePersist
    @PreUpdate
    public void actualizarFecha() {

        fechaActualizacion = LocalDateTime.now();

    }

}