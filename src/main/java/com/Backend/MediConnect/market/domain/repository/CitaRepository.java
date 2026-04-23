package com.Backend.MediConnect.market.domain.repository;

import com.Backend.MediConnect.market.persistance.entity.Cita;
import com.Backend.MediConnect.market.persistance.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;


public interface CitaRepository extends JpaRepository<Cita, Long>{
    List<Cita> findByFecha(LocalDate fecha);
    List<Cita> findByMedico(Medico medico);
}
