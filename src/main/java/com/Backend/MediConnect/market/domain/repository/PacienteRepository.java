package com.Backend.MediConnect.market.domain.repository;

import com.Backend.MediConnect.market.persistance.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

}
