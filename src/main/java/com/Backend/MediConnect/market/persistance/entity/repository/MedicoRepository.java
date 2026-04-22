package com.Backend.MediConnect.market.persistance.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Backend.MediConnect.market.persistance.entity.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {
    // Aquí podrías buscar por DNI si lo necesitas más adelante
    Medico findByDni(String dni);
}