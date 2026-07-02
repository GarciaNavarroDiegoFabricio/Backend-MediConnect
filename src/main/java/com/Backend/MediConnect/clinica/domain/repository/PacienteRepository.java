package com.Backend.MediConnect.clinica.domain.repository;

import com.Backend.MediConnect.clinica.persistance.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    
    Optional<Paciente> findByDni(String dni);

    boolean existsByDni(String dni);
    
    // REQUERIMIENTO: Búsqueda unificada en todos los nombres
    @Query("SELECT p FROM Paciente p LEFT JOIN p.historiaClinica h " +
           "WHERE p.dni LIKE %:termino% " +
           "OR LOWER(p.primerNombre) LIKE LOWER(CONCAT('%', :termino, '%')) " +
           "OR LOWER(p.segundoNombre) LIKE LOWER(CONCAT('%', :termino, '%')) " +
           "OR LOWER(p.primerApellido) LIKE LOWER(CONCAT('%', :termino, '%')) " +
           "OR LOWER(p.segundoApellido) LIKE LOWER(CONCAT('%', :termino, '%')) " +
           "OR CAST(p.idPaciente AS string) = :termino " +
           "OR CAST(h.idHistoria AS string) = :termino")
    List<Paciente> buscarPacientesPorFiltro(@Param("termino") String termino);
}