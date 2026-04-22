package com.Backend.MediConnect.market.domain.dto;

import java.time.LocalTime;

public class consultarHorarioDTO {
    private Integer idHorario;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer intervaloMinutos;
    private String estado;

    // Getters y Setters
    public Integer getIdHorario() { 
        return idHorario; 
    }

    public void setIdHorario(Integer idHorario) { 
        this.idHorario = idHorario; 
    }

    public String getDiaSemana() { 
        return diaSemana; 
    }

    public void setDiaSemana(String diaSemana) { 
        this.diaSemana = diaSemana; 
    }

    public LocalTime getHoraInicio() { 
        return horaInicio; 
    }

    public void setHoraInicio(LocalTime horaInicio) { 
        this.horaInicio = horaInicio; 
    }

    public LocalTime getHoraFin() { 
        return horaFin; 
    }

    public void setHoraFin(LocalTime horaFin) {
         this.horaFin = horaFin; 
        }

    public Integer getIntervaloMinutos() { 
        return intervaloMinutos; 
    }

    public void setIntervaloMinutos(Integer intervaloMinutos) { 
        this.intervaloMinutos = intervaloMinutos; 
    }

    public String getEstado() { 
        return estado; 
    }

    public void setEstado(String estado) { 
        this.estado = estado; 
    }   
}