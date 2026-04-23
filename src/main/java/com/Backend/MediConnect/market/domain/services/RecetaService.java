package com.Backend.MediConnect.market.domain.services;

import com.Backend.MediConnect.market.domain.dto.RecetaDTO;
import com.Backend.MediConnect.market.domain.repository.*;
import com.Backend.MediConnect.market.persistance.entity.*;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de la lógica de negocio relacionada a las recetas médicas.
 */
@Service
public class RecetaService {
    // Repositorios para acceder a las entidades relacionadas
    private final RecetaRepository recetaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final ConsultaRepository consultaRepository;

    // Inyección de dependencias mediante constructor
    public RecetaService(RecetaRepository recetaRepository,
                         PacienteRepository pacienteRepository,
                         MedicoRepository medicoRepository,
                         ConsultaRepository consultaRepository) {
        this.recetaRepository = recetaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.consultaRepository = consultaRepository;
    }

    //Crea una nueva receta médica a partir de los datos recibidos en el DTO.
    public Receta crearReceta(RecetaDTO dto) {

        // Busca el paciente por ID, lanza excepción si no existe
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Busca al doctor por ID, lanza excepción si no existe
        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Medico no encontrado"));

        // Busca consulta por ID, lanza excepción si no existe
        Consulta consulta = consultaRepository.findById(dto.getConsultaId())
                .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        // Asigna las relaciones y datos provenientes del DTO
        Receta receta = new Receta();
        receta.setPaciente(paciente);
        receta.setMedico(medico);
        receta.setConsulta(consulta);
        receta.setPrescripcion(dto.getPrescripcion());
        receta.setFecha(dto.getFecha());

        return recetaRepository.save(receta);
    }
}
