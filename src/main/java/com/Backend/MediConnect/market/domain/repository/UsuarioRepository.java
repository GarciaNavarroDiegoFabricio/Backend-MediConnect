package com.Backend.MediConnect.market.domain.repository;

import com.Backend.MediConnect.market.persistance.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByDni(String dni);
    boolean existsByDni(String dni);
}