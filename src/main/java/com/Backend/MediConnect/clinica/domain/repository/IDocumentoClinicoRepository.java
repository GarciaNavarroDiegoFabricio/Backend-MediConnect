package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.DocumentoClinico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDocumentoClinicoRepository extends JpaRepository<DocumentoClinico, Long> {
    List<DocumentoClinico> findByConsulta_IdConsulta(Long idConsulta);
}