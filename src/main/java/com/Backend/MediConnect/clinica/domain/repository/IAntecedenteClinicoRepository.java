package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.AntecedenteClinico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAntecedenteClinicoRepository extends JpaRepository<AntecedenteClinico, Long> {
    List<AntecedenteClinico> findByHistoriaClinica_IdHistoria(Long idHistoria);
}