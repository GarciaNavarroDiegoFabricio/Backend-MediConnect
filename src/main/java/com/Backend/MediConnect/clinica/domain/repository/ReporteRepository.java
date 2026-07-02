package com.Backend.MediConnect.clinica.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Backend.MediConnect.clinica.persistance.entity.Reporte;

public interface ReporteRepository extends JpaRepository<Reporte, Integer> {
}