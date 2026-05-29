package com.Backend.MediConnect.market.persistance.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "HISTORIA_CLINICA")
public class HistoriaClinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historia")
    private Integer idHistoria;

    private LocalDate fecha;

    @Column(name = "motivo_ingreso")
    private String motivoIngreso;

    @OneToOne
    @JoinColumn(name = "id_paciente")
    private Paciente paciente;

    @Column(name = "historia_enfermedad_actual")
    private String historiaEnfermedadActual;

    @Column(name = "enfermedades_pasadas")
    private String enfermedadesPasadas;

    public HistoriaClinica(){};

    public Integer getIdHistoria() {
        return idHistoria;
    }

    public void setIdHistoria(Integer idHistoria) {
        this.idHistoria = idHistoria;
    }

    @Column(name ="codigo_unico", unique = true)
    private String codigoUnico;
    
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getMotivoIngreso() {
        return motivoIngreso;
    }

    public void setMotivoIngreso(String motivoIngreso) {
        this.motivoIngreso = motivoIngreso;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getHistoriaEnfermedadActual() {
        return historiaEnfermedadActual;
    }

    public void setHistoriaEnfermedadActual(String historiaEnfermedadActual) {
        this.historiaEnfermedadActual = historiaEnfermedadActual;
    }

    public String getEnfermedadesPasadas() {
        return enfermedadesPasadas;
    }

    public void setEnfermedadesPasadas(String enfermedadesPasadas) {
        this.enfermedadesPasadas = enfermedadesPasadas;
    }
}
