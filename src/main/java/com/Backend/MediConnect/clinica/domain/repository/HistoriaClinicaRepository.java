package com.Backend.MediConnect.clinica.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Backend.MediConnect.clinica.persistance.entity.HistoriaClinica;

public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Integer> {
    boolean existsByCodigoUnico(String codigoUnico);
}