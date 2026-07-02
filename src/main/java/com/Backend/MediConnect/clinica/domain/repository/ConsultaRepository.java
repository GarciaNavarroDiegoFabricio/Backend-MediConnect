package com.Backend.MediConnect.clinica.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Backend.MediConnect.clinica.persistance.entity.Consulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {
}