package com.Backend.MediConnect.clinica.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Backend.MediConnect.clinica.persistance.entity.DetalleAtencionMedica;

public interface IDetalleAtencionMedicaRepository
        extends JpaRepository<DetalleAtencionMedica, Long> {

    Optional<DetalleAtencionMedica> findByConsulta_IdConsulta(Long idConsulta);

}