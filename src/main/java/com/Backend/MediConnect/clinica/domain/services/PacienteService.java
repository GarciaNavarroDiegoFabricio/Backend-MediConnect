package com.Backend.MediConnect.clinica.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Backend.MediConnect.clinica.domain.dto.CitaDTO;
import com.Backend.MediConnect.clinica.domain.dto.CitaResponseDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.IPacienteService;
import com.Backend.MediConnect.clinica.domain.repository.CitaRepository;
import com.Backend.MediConnect.clinica.domain.repository.MedicoRepository;
import com.Backend.MediConnect.clinica.domain.repository.PacienteRepository;
import com.Backend.MediConnect.clinica.domain.repository.SedeRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Cita;
import com.Backend.MediConnect.clinica.persistance.entity.Medico;
import com.Backend.MediConnect.clinica.persistance.entity.Paciente;
import com.Backend.MediConnect.clinica.persistance.entity.Sede;
import com.Backend.MediConnect.clinica.web.mapper.CitaMapper;

@Service
public class PacienteService implements IPacienteService {

    private final PacienteRepository pacienteRepo;
    private final MedicoRepository medicoRepo;
    private final SedeRepository sedeRepo;
    private final CitaRepository citaRepo;

    public PacienteService(PacienteRepository pacienteRepo,
            MedicoRepository medicoRepo,
            SedeRepository sedeRepo,
            CitaRepository citaRepo) {
        this.pacienteRepo = pacienteRepo;
        this.medicoRepo = medicoRepo;
        this.sedeRepo = sedeRepo;
        this.citaRepo = citaRepo;
    }

    @Override
    @Transactional
    public CitaResponseDTO generarCita(String dniPaciente, CitaDTO dto) {
        Paciente paciente = pacienteRepo.findByDni(dniPaciente)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Medico medico = medicoRepo.findById(dto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        if (!medico.getDisponible())
            throw new RuntimeException("El médico no está disponible");

        Sede sede = sedeRepo.findById(dto.getIdSede())
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setSede(sede);
        cita.setFecha(dto.getFecha());
        cita.setHora(dto.getHora());
        cita.setEspecialidad(dto.getEspecialidad());
        cita.setTipo(dto.getTipo());
        cita.setPrioridad(dto.getPrioridad());
        cita.setDuracionEstimada(dto.getDuracionEstimada());
        cita.setEstado("PENDIENTE");

       return CitaMapper.toResponse(citaRepo.save(cita));
    }

    public List<CitaResponseDTO> consultarCitas(String dniPaciente) {
    Paciente paciente = pacienteRepo.findByDni(dniPaciente)
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    return citaRepo.findByPaciente(paciente)
            .stream()
            .map(CitaMapper::toResponse) 
            .collect(Collectors.toList());
}

    @Override
    @Transactional
    public void cancelarCita(String dniPaciente, Integer idCita) {
        Cita cita = citaRepo.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (!cita.getPaciente().getDni().equals(dniPaciente))
            throw new RuntimeException("No autorizado para cancelar esta cita");

        if (cita.getEstado().equals("CANCELADA"))
            throw new RuntimeException("La cita ya está cancelada");

        cita.setEstado("CANCELADA");
        citaRepo.save(cita);
    }
}