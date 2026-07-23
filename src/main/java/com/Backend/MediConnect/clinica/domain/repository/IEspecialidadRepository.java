package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEspecialidadRepository extends JpaRepository<Especialidad, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}