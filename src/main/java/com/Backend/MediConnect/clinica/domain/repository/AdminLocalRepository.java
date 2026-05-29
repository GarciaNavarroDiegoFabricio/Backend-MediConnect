package com.Backend.MediConnect.clinica.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Backend.MediConnect.clinica.persistance.entity.AdministadorLocal;

import java.util.Optional;

public interface AdminLocalRepository extends JpaRepository<AdministadorLocal, Integer> {
    Optional<AdministadorLocal> findByDni(String dni);

    boolean existsByDni(String dni);
}