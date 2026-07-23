package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.Sede;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISedeRepository extends JpaRepository<Sede, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}