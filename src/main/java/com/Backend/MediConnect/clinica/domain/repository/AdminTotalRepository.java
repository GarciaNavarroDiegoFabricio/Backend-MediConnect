package com.Backend.MediConnect.clinica.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Backend.MediConnect.clinica.persistance.entity.AdministradorTotal;

import java.util.Optional;

public interface AdminTotalRepository extends JpaRepository<AdministradorTotal, Integer> {
    Optional<AdministradorTotal> findByDni(String dni);

    boolean existsByDni(String dni);
}