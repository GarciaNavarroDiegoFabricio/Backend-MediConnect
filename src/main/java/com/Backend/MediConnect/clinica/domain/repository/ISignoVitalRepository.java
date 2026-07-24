package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.SignoVital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ISignoVitalRepository extends JpaRepository<SignoVital, Long> {
    Optional<SignoVital> findByConsulta_IdConsulta(Long idConsulta);
}