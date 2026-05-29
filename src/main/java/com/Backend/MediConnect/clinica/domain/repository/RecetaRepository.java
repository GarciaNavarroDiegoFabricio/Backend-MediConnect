package com.Backend.MediConnect.clinica.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Backend.MediConnect.clinica.persistance.entity.Receta;

public interface RecetaRepository extends JpaRepository<Receta, Integer> {
}