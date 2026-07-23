package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.BloqueoHorario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IBloqueoHorarioRepository extends JpaRepository<BloqueoHorario, Long> {
    List<BloqueoHorario> findByMedico_IdMedico(Long idMedico);
    List<BloqueoHorario> findByMedico_IdMedicoAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
            Long idMedico, LocalDate fecha1, LocalDate fecha2);
}