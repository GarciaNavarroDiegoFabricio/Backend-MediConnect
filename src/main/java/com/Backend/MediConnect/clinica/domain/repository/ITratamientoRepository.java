package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.Tratamiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITratamientoRepository extends JpaRepository<Tratamiento, Long> {
    List<Tratamiento> findByAtencionMedica_IdAtencion(Long idAtencion);
}