package com.Backend.MediConnect.market.domain.repository;

import com.Backend.MediConnect.market.persistance.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {
    //findBy=Select * from CITA,  Paciente indica que en la entidad Cita hay un atributo
    //que es un objeto de tipo Paciente, el _IdPaciente significa que JPA buscara dentro de ese objeto
    //el idPaciente para buscarlo en la BD como id_paciente.
    // Es igual a decir Select * from CITA where id_paciente= idPaciente
    List<Cita> findByPaciente_IdPaciente(Integer idPaciente);
}
