package com.Backend.MediConnect.market.domain.dto;

import java.time.LocalDate;
import java.time.LocalTime;

//Estos son los datos que se envían al cliente para representar una reserva de su cita medica
public class ReservaDTO {
    private Long citaId;
    private String pacienteNombre;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;
    private String especialidad;

    // Constructor
    public ReservaDTO(Long citaId, String pacienteNombre, LocalDate fecha, LocalTime hora, String estado, String especialidad) {
        this.citaId = citaId;
        this.pacienteNombre = pacienteNombre;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.especialidad = especialidad;
    }

    //Getters y setters

    public Long getCitaId() {
        return citaId;
    }

    public void setCitaId(Long citaId) {
        this.citaId = citaId;
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public void setPacienteNombre(String pacienteNombre) {
        this.pacienteNombre = pacienteNombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}
