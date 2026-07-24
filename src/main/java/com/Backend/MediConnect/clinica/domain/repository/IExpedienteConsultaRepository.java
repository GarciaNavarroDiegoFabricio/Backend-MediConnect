package com.Backend.MediConnect.clinica.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Backend.MediConnect.clinica.persistance.entity.ExpedienteConsulta;

public interface IExpedienteConsultaRepository
        extends JpaRepository<ExpedienteConsulta, Long> {

    List<ExpedienteConsulta> findByExpediente_IdExpediente(Long idExpediente);

}