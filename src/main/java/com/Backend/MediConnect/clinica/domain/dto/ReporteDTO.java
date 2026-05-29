package com.Backend.MediConnect.clinica.domain.dto;

import java.time.LocalDate;

public class ReporteDTO {

    private LocalDate fechaReporte;
    private int citasAtendidas;
    private int citasCanceladas;
    private int citasReprogramadas;
    private int citasPendientes;

    public ReporteDTO() {

    }

    // Constructor
    public ReporteDTO(LocalDate fechaReporte, int atendidas, int canceladas, int reprogramadas, int pendientes) {
        this.fechaReporte = fechaReporte;
        this.citasAtendidas = atendidas;
        this.citasCanceladas = canceladas;
        this.citasReprogramadas = reprogramadas;
        this.citasPendientes = pendientes;
    }

    // Getter y Setters
    public LocalDate getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(LocalDate fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public int getCitasAtendidas() {
        return citasAtendidas;
    }

    public void setCitasAtendidas(int citasAtendidas) {
        this.citasAtendidas = citasAtendidas;
    }

    public int getCitasCanceladas() {
        return citasCanceladas;
    }

    public void setCitasCanceladas(int citasCanceladas) {
        this.citasCanceladas = citasCanceladas;
    }

    public int getCitasReprogramadas() {
        return citasReprogramadas;
    }

    public void setCitasReprogramadas(int citasReprogramadas) {
        this.citasReprogramadas = citasReprogramadas;
    }

    public int getCitasPendientes() {
        return citasPendientes;
    }

    public void setCitasPendientes(int citasPendientes) {
        this.citasPendientes = citasPendientes;
    }
}
