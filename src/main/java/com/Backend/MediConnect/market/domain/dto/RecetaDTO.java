package com.Backend.MediConnect.market.domain.dto;

import java.time.LocalDate;

public class RecetaDTO {
    private Long pacienteId;
    private Long medicoId;
    private Long consultaId;
    private String prescripcion;
    private LocalDate fecha;

    //Getters y Setterss

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Long getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(Long medicoId) {
        this.medicoId = medicoId;
    }

    public Long getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Long consultaId) {
        this.consultaId = consultaId;
    }

    public String getPrescripcion() {
        return prescripcion;
    }

    public void setPrescripcion(String prescripcion) {
        this.prescripcion = prescripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
