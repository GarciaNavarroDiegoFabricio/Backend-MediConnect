package com.Backend.MediConnect.clinica.domain.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;

import com.Backend.MediConnect.clinica.domain.dto.PacienteResponseDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.IPacienteConsultaService;
import com.Backend.MediConnect.clinica.domain.repository.PacienteRepository;
import com.Backend.MediConnect.clinica.domain.repository.ConsultaRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Consulta;
import com.Backend.MediConnect.clinica.web.mapper.EntityMapper;

@Service
public class PacienteConsultaService implements IPacienteConsultaService {

    private final PacienteRepository pacienteRepo;
    private final ConsultaRepository consultaRepository;

    public PacienteConsultaService(PacienteRepository pacienteRepo, ConsultaRepository consultaRepository) {
        this.pacienteRepo = pacienteRepo;
        this.consultaRepository = consultaRepository;
    }

    @Override
    public PacienteResponseDTO buscarPorDni(String dni) {
        if (dni == null || !dni.matches("\\d{8}")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El DNI debe tener 8 dígitos numéricos");
        }

        return pacienteRepo.findByDni(dni)
                .map(EntityMapper::toPacienteResponse)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente no encontrado"));
    }

    // Se añade el nuevo método de finalización
    @Override
    public void finalizarConsulta(Integer idConsulta) {
        Consulta consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta no encontrada"));

        if (consulta.getFechaHoraFin() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La consulta ya fue finalizada anteriormente");
        }

        consulta.setFechaHoraFin(LocalDateTime.now());
        consultaRepository.save(consulta);
    }
}