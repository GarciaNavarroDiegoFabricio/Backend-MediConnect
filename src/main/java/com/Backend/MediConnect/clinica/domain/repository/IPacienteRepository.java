package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByPersona_IdPersona(Long idPersona);
    Optional<Paciente> findByPersona_Usuario_IdUsuario(Long idUsuario);
    Optional<Paciente> findByCodigoHistoriaClinica(String codigo);
    boolean existsByCodigoHistoriaClinica(String codigo);
}