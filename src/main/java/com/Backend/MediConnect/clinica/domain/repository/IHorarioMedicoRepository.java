package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.HorarioMedico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IHorarioMedicoRepository extends JpaRepository<HorarioMedico, Long> {
    List<HorarioMedico> findByMedico_IdMedico(Long idMedico);
    List<HorarioMedico> findByMedico_IdMedicoAndEstado(Long idMedico, String estado);
}