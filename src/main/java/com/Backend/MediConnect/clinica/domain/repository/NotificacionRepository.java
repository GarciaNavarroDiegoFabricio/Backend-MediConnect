package com.Backend.MediConnect.clinica.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Backend.MediConnect.clinica.persistance.entity.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
}
