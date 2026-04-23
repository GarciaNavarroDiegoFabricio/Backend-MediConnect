package com.Backend.MediConnect.market.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Backend.MediConnect.market.persistance.entity.Medico;
import com.Backend.MediConnect.market.persistance.entity.repository.MedicoRepository;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    // Tarea: GestionarMedico (Guardar/Actualizar)
    public Medico guardarMedico(Medico medico) {
        return medicoRepository.save(medico);
    }

    public Optional<Medico> obtenerPorId(Integer id) {
        return medicoRepository.findById(id);
    }

    public void eliminarMedico(Integer id) {
        medicoRepository.deleteById(id);
    }

    // Tarea: CambiarDisponibilidad
    public Medico cambiarDisponibilidad(Integer id) {
        return medicoRepository.findById(id).map(medico -> {
            medico.setDisponible(!medico.getDisponible());
            return medicoRepository.save(medico);
        }).orElse(null);
    }
}