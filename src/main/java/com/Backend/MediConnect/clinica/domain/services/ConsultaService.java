package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.ConsultaContextoDTO;
import com.Backend.MediConnect.clinica.domain.repository.CitaRepository;
import com.Backend.MediConnect.clinica.domain.repository.ConsultaRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Cita;
import com.Backend.MediConnect.clinica.persistance.entity.Consulta;
import com.Backend.MediConnect.clinica.persistance.entity.Paciente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private CitaRepository citaRepository;

    public Consulta iniciarConsulta(Integer citaId) {

        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (consultaRepository.existsByCita(cita)) {
            throw new RuntimeException("Ya existe una consulta para esta cita");
        }

        Consulta consulta = new Consulta();
        consulta.setCita(cita);
        consulta.setMedico(cita.getMedico());
        consulta.setPaciente(cita.getPaciente());
        consulta.setHoraInicio(LocalDateTime.now());
        consulta.setEstado("EN_PROCESO");

        return consultaRepository.save(consulta);
    }

    public Consulta finalizarConsulta(Integer consultaId) {

        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        consulta.setHoraFin(LocalDateTime.now());
        consulta.setEstado("FINALIZADA");

        return consultaRepository.save(consulta);
    }

    private Integer calcularEdad(java.time.LocalDate fechaNacimiento) {
        if (fechaNacimiento == null)
            return 0;

        return java.time.Period.between(
                fechaNacimiento,
                java.time.LocalDate.now()).getYears();
    }

    public ConsultaContextoDTO obtenerContexto(Integer consultaId) {

        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        Paciente p = consulta.getPaciente();
        Cita c = consulta.getCita();

        ConsultaContextoDTO dto = new ConsultaContextoDTO();

        // Paciente
        dto.setIdPaciente(p.getIdPaciente());
        dto.setNombrePaciente(
                p.getPrimerNombre() + " " + p.getPrimerApellido());
        dto.setDni(p.getDni());

        // si no tienes edad calculada real, fallback simple
        dto.setEdad(calcularEdad(p.getFechaNacimiento()));

        // Cita
        dto.setEspecialidad(c.getEspecialidad());
        dto.setHora(c.getHora());

        // si no tienes motivo explícito, puedes usar tipo o especialidad
        dto.setMotivo(c.getTipo() != null ? c.getTipo() : "Consulta médica");

        // Médico
        dto.setMedico(
                consulta.getMedico().getPrimerNombre() + " " +
                        consulta.getMedico().getPrimerApellido());

        return dto;
    }
}