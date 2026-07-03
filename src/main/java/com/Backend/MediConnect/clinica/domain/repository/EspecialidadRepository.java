package com.Backend.MediConnect.clinica.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Backend.MediConnect.clinica.persistance.entity.Especialidad;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {

    boolean existsByNombreEspecialidad(String nombreEspecialidad);

    Optional<Especialidad> findByNombreEspecialidad(String nombreEspecialidad);

    List<Especialidad> findByActivo(Boolean activo);
}