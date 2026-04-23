package com.Backend.MediConnect.market.domain.repository;

import com.Backend.MediConnect.market.persistance.entity.AdministadorLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminLocalRepository extends JpaRepository<AdministadorLocal, Integer> {
    Optional<AdministadorLocal> findByDni(String dni);
    boolean existsByDni(String dni);
}