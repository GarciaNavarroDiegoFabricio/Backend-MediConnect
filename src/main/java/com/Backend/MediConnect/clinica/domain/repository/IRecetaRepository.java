package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.Receta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IRecetaRepository extends JpaRepository<Receta, Long> {
    Optional<Receta> findByAtencionMedica_IdAtencion(Long idAtencion);
    boolean existsByCodigoReceta(String codigoReceta);
    List<Receta> findByAtencionMedica_Cita_Paciente_IdPaciente(Long idPaciente);
}