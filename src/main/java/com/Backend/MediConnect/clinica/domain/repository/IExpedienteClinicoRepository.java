package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.ExpedienteClinico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IExpedienteClinicoRepository
        extends JpaRepository<ExpedienteClinico, Long> {

    Optional<ExpedienteClinico> findByPaciente_IdPaciente(Long idPaciente);

}