package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByDni(String dni);
    Optional<Persona> findByUsuario_IdUsuario(Long idUsuario);
    boolean existsByDni(String dni);
}