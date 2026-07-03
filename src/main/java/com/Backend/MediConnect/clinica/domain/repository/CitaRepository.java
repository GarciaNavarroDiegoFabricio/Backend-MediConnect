package com.Backend.MediConnect.clinica.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Backend.MediConnect.clinica.persistance.entity.Cita;
import com.Backend.MediConnect.clinica.persistance.entity.Medico;
import com.Backend.MediConnect.clinica.persistance.entity.Paciente;

public interface CitaRepository extends JpaRepository<Cita, Integer> {
    List<Cita> findByPaciente(Paciente paciente);

    List<Cita> findByMedicoAndEstadoOrderByFechaAscHoraAsc(
            Medico medico,
            String estado);

    List<Cita> findByMedicoAndEstado(Medico medico, String estado);

    // AGREGADO PARA EL RF2: Buscar citas activas de hoy
    List<Cita> findByMedicoAndFechaAndEstadoNot(Medico medico, java.time.LocalDate fecha, String estado);

    Optional<Cita> findById(Integer idCita);

    List<Cita> findByMedico(Medico medico);
}