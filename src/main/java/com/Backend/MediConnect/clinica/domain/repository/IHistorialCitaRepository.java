package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.HistorialCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IHistorialCitaRepository extends JpaRepository<HistorialCita, Long> {
    List<HistorialCita> findByCita_IdCitaOrderByFechaCambioDesc(Long idCita);
}