package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "reporte")
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Integer idReporte;

    @Column(name = "fecha_reporte")
    private LocalTime fechaReporte;

    @Column(name = "citas_atendidas")
    private Integer citasAtendidas;

    @Column(name = "citas_canceladas")
    private Integer citasCanceladas;

    @Column(name = "citas_reprogramadas")
    private Integer citasReprogramadas;

    @Column(name = "citas_pendientes")
    private Integer citasPendientes;

    @ManyToOne
    @JoinColumn(name = "id_admin_local")
    private AdministadorLocal adminLocal;

    public Reporte() {
    };

    public Integer getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Integer idReporte) {
        this.idReporte = idReporte;
    }

    public LocalTime getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(LocalTime fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public Integer getCitasAtendidas() {
        return citasAtendidas;
    }

    public void setCitasAtendidas(Integer citasAtendidas) {
        this.citasAtendidas = citasAtendidas;
    }

    public Integer getCitasCanceladas() {
        return citasCanceladas;
    }

    public void setCitasCanceladas(Integer citasCanceladas) {
        this.citasCanceladas = citasCanceladas;
    }

    public Integer getCitasReprogramadas() {
        return citasReprogramadas;
    }

    public void setCitasReprogramadas(Integer citasReprogramadas) {
        this.citasReprogramadas = citasReprogramadas;
    }

    public Integer getCitasPendientes() {
        return citasPendientes;
    }

    public void setCitasPendientes(Integer citasPendientes) {
        this.citasPendientes = citasPendientes;
    }

    public AdministadorLocal getAdminLocal() {
        return adminLocal;
    }

    public void setAdminLocal(AdministadorLocal adminLocal) {
        this.adminLocal = adminLocal;
    }
}
