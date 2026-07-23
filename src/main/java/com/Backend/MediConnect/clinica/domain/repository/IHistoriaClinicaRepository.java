package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IHistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {
    Optional<HistoriaClinica> findByPaciente_IdPaciente(Long idPaciente);
    boolean existsByPaciente_IdPaciente(Long idPaciente);
}