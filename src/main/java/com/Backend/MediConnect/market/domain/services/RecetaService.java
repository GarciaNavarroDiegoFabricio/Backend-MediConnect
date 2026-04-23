package com.Backend.MediConnect.market.domain.services;

import com.Backend.MediConnect.market.domain.dto.RecetaDTO;
import com.Backend.MediConnect.market.domain.repository.*;
import com.Backend.MediConnect.market.persistance.entity.*;
import org.springframework.stereotype.Service;

@Service
public class RecetaService {
    private final RecetaRepository recetaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final ConsultaRepository consultaRepository;

    public RecetaService(RecetaRepository recetaRepository,
                         PacienteRepository pacienteRepository,
                         MedicoRepository medicoRepository,
                         ConsultaRepository consultaRepository) {
        this.recetaRepository = recetaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.consultaRepository = consultaRepository;
    }

    public Receta crearReceta(RecetaDTO dto) {

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Medico no encontrado"));

        Consulta consulta = consultaRepository.findById(dto.getConsultaId())
                .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        Receta receta = new Receta();
        receta.setPaciente(paciente);
        receta.setMedico(medico);
        receta.setConsulta(consulta);
        receta.setPrescripcion(dto.getPrescripcion());
        receta.setFecha(dto.getFecha());

        return recetaRepository.save(receta);
    }
}
