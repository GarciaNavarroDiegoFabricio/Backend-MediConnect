package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDiagnosticoRepository extends JpaRepository<Diagnostico, Long> {
    List<Diagnostico> findByAtencionMedica_IdAtencion(Long idAtencion);
}