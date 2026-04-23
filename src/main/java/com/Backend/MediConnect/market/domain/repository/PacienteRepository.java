package com.Backend.MediConnect.market.domain.repository;

import com.Backend.MediConnect.market.persistance.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    Optional<Paciente> findByDni(String dni);
    boolean existsByDni(String dni);
}