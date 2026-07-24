package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IConsultaRepository
        extends JpaRepository<Consulta, Long> {

    List<Consulta> findByPaciente_IdPaciente(Long idPaciente);

    List<Consulta> findByMedico_IdMedico(Long idMedico);

    Optional<Consulta> findByCita_IdCita(Long idCita);

}