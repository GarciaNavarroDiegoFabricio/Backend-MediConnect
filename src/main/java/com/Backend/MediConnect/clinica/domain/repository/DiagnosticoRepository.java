package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Integer> {

    List<Diagnostico> findByConsulta_Paciente_IdPaciente(Integer idPaciente);

    List<Diagnostico> findByConsulta_Medico_IdMedico(Integer idMedico);
}