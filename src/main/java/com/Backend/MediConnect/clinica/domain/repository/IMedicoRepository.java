package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IMedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByPersona_IdPersona(Long idPersona);
    Optional<Medico> findByPersona_Usuario_IdUsuario(Long idUsuario);
    Optional<Medico> findByPersona_Dni(String dni);
    boolean existsByNumeroColegiatura(String numeroColegiatura);
    List<Medico> findByEspecialidad_IdEspecialidadAndEstado(Long idEspecialidad, String estado);
    List<Medico> findByPersona_Usuario_IdSedeAndEstado(Long idSede, String estado);
    List<Medico> findByEstadoAndDisponible(String estado, Boolean disponible);
}