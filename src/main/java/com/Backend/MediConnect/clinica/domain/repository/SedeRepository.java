package com.Backend.MediConnect.clinica.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Backend.MediConnect.clinica.persistance.entity.Sede;

public interface SedeRepository extends JpaRepository<Sede, Integer> {

    boolean existsByNombreSede(String nombreSede);

    Optional<Sede> findByNombreSede(String nombreSede);

    List<Sede> findByActivo(Boolean activo);

    List<Sede> findByUbicacionContainingIgnoreCase(String ubicacion);
}