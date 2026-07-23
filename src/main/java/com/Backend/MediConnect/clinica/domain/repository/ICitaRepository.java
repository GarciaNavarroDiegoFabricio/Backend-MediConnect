package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ICitaRepository extends JpaRepository<Cita, Long> {
    Optional<Cita> findByIdCita(Long idCita);
    List<Cita> findByMedico_IdMedicoAndFechaCita(Long idMedico, LocalDate fechaCita);
    List<Cita> findByPaciente_IdPaciente(Long idPaciente);
    List<Cita> findByMedico_IdMedico(Long idMedico);
    List<Cita> findByEstado(String estado);
    List<Cita> findByFechaCitaAndEstado(LocalDate fechaCita, String estado);
    List<Cita> findByPaciente_Persona_Usuario_IdUsuario(Long idUsuario);
}