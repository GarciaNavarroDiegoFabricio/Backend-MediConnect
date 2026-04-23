package com.Backend.MediConnect.market.domain.dto;

import java.time.LocalTime;

public class crearHorarioDTO {
    private Integer idMedico;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer intervaloMinutos;

    // Getters y Setters
    public Integer getIdMedico() { 
        return idMedico;
     }

    public void setIdMedico(Integer idMedico) { 
        this.idMedico = idMedico; 
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
}