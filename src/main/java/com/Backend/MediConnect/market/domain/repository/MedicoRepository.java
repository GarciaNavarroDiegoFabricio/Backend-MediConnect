package com.Backend.MediConnect.market.domain.repository;

import com.Backend.MediConnect.market.persistance.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Integer> {
    Optional<Medico> findByDni(String dni);
    boolean existsByDni(String dni);
}