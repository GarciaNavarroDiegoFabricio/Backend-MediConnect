package com.Backend.MediConnect.market.persistance.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Backend.MediConnect.market.persistance.entity.Horario;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Integer> {
    // Útil para ver todos los horarios de un médico específico
    List<Horario> findByMedicoIdMedico(Integer idMedico);
}