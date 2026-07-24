package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.AntecedentePaciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAntecedenteClinicoRepository extends JpaRepository<AntecedentePaciente, Long> {
    List<AntecedentePaciente> findByExpediente_IdExpediente(Long idExpediente);
}