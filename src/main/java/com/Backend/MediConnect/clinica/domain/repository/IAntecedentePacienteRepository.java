package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.AntecedentePaciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAntecedentePacienteRepository
        extends JpaRepository<AntecedentePaciente, Long> {

    Optional<AntecedentePaciente> findByExpediente_IdExpediente(Long idExpediente);

}