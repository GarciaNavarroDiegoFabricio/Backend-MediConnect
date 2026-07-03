package com.Backend.MediConnect.clinica.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Backend.MediConnect.clinica.persistance.entity.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Integer> {
    Optional<Medico> findByDni(String dni);

    boolean existsByDni(String dni);

    List<Medico> findBySedesIdSede(Integer idSede);

    // AGREGADO PARA EL RF5: Traer solo médicos "apagados"
    List<Medico> findByDisponibleFalse();

    boolean existsByNumeroColegiatura(String numeroColegiatura);
}