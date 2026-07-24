package com.Backend.MediConnect.clinica.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Backend.MediConnect.clinica.persistance.entity.DiagnosticoMedico;

public interface IDiagnosticoMedicoRepository
        extends JpaRepository<DiagnosticoMedico, Long> {

    List<DiagnosticoMedico> findByConsulta_IdConsulta(Long idConsulta);

}