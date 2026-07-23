package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.AtencionMedica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAtencionMedicaRepository extends JpaRepository<AtencionMedica, Long> {
    Optional<AtencionMedica> findByCita_IdCita(Long idCita);
    List<AtencionMedica> findByHistoriaClinica_IdHistoriaOrderByFechaAtencionDesc(Long idHistoria);
}