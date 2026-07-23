package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.DetalleReceta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDetalleRecetaRepository extends JpaRepository<DetalleReceta, Long> {
    List<DetalleReceta> findByReceta_IdReceta(Long idReceta);
}