package com.Backend.MediConnect.clinica.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Backend.MediConnect.clinica.persistance.entity.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Integer> {
}