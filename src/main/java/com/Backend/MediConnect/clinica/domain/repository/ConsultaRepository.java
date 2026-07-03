package com.Backend.MediConnect.clinica.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Backend.MediConnect.clinica.persistance.entity.Cita;
import com.Backend.MediConnect.clinica.persistance.entity.Consulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {
    Optional<Consulta> findByCita(Cita cita);

    boolean existsByCita(Cita cita);

    List<Consulta> findByPaciente_IdPaciente(Integer idPaciente);
}