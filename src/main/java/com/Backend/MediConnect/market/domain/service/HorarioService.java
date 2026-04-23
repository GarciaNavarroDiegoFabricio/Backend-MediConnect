package com.Backend.MediConnect.market.domain.service;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Backend.MediConnect.market.persistance.entity.Horario;
import com.Backend.MediConnect.market.persistance.entity.repository.HorarioRepository;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    // Tarea: ReprogramarHorario
    public Horario reprogramar(Integer idHorario, LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin) {
        return horarioRepository.findById(idHorario).map(horario -> {
            horario.setHoraInicio(nuevaHoraInicio);
            horario.setHoraFin(nuevaHoraFin);
            horario.setEstado("REPROGRAMADO"); // Marcamos el cambio de estado
            return horarioRepository.save(horario);
        }).orElse(null);
    }
}