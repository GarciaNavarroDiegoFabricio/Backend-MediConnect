package com.Backend.MediConnect.clinica.domain.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class cancelarCitaDTO {
    // Estos son los datos que el paciente debe enviar en su peticion, para crear
    // una nueva cita sin revelar
    // nuestra tabla y entity.
    private Integer idCita;
    private String estado;
    private String fecha;
    private String hora;

    public cancelarCitaDTO() {
    };

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
