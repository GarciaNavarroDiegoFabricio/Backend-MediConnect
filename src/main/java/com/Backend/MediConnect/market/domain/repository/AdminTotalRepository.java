package com.Backend.MediConnect.market.domain.repository;

import com.Backend.MediConnect.market.persistance.entity.AdministradorTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminTotalRepository extends JpaRepository<AdministradorTotal, Integer> {
    Optional<AdministradorTotal> findByDni(String dni);
    boolean existsByDni(String dni);
}