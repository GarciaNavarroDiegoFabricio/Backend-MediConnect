package com.Backend.MediConnect.clinica.domain.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Backend.MediConnect.clinica.domain.dto.PacienteResponseDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.IPacienteConsultaService;
import com.Backend.MediConnect.clinica.domain.repository.PacienteRepository;
import com.Backend.MediConnect.clinica.web.mapper.EntityMapper;

@Service
public class PacienteConsultaService implements IPacienteConsultaService {

    private final PacienteRepository pacienteRepo;

    public PacienteConsultaService(PacienteRepository pacienteRepo) {
        this.pacienteRepo = pacienteRepo;
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
}