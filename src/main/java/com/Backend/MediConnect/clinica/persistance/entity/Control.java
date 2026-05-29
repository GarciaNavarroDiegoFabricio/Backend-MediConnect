package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "CONTROL")
public class Control {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_control")
    private Integer idControl;

    @ManyToOne
    @JoinColumn(name = "id_medico")
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    private Paciente paciente;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "analisis_realizados")
    private String analisisRealizados;

    @Column(name = "revision_medica")
    private String revisionMedica;

    public Control() {
    };

    public Integer getIdControl() {
        return idControl;
    }

    public void setIdControl(Integer idControl) {
        this.idControl = idControl;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getAnalisisRealizados() {
        return analisisRealizados;
    }

    public void setAnalisisRealizados(String analisisRealizados) {
        this.analisisRealizados = analisisRealizados;
    }

    public String getRevisionMedica() {
        return revisionMedica;
    }

    public void setRevisionMedica(String revisionMedica) {
        this.revisionMedica = revisionMedica;
    }
}
