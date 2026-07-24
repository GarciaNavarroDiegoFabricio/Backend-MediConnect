package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.request.AntecedentePacienteRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.AntecedentePacienteResponseDTO;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.repository.IAntecedentePacienteRepository;
import com.Backend.MediConnect.clinica.domain.repository.IExpedienteClinicoRepository;
import com.Backend.MediConnect.clinica.persistance.entity.AntecedentePaciente;
import com.Backend.MediConnect.clinica.persistance.entity.ExpedienteClinico;
import com.Backend.MediConnect.clinica.web.mapper.AntecedentePacienteMapper;
import com.Backend.MediConnect.clinica.web.mapper.HistoriaClinicaMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AntecedentePacienteService {

        private final IAntecedentePacienteRepository antecedenteRepository;
        private final IExpedienteClinicoRepository expedienteRepository;
        private final AntecedentePacienteMapper mapper;

        public AntecedentePacienteService(
                        IAntecedentePacienteRepository antecedenteRepository,
                        IExpedienteClinicoRepository expedienteRepository,
                        AntecedentePacienteMapper mapper) {

                this.antecedenteRepository = antecedenteRepository;
                this.expedienteRepository = expedienteRepository;
                this.mapper = mapper;

        }

        public AntecedentePacienteResponseDTO obtener(Long idPaciente) {

                ExpedienteClinico expediente = expedienteRepository
                                .findByPaciente_IdPaciente(idPaciente)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "El paciente no tiene expediente clínico"));

                AntecedentePaciente antecedente = antecedenteRepository
                                .findByExpediente_IdExpediente(
                                                expediente.getIdExpediente())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "El paciente no tiene antecedentes registrados"));

                return mapper.toResponse(antecedente);

        }

        @Transactional
        public AntecedentePacienteResponseDTO actualizar(
                        Long idPaciente,
                        AntecedentePacienteRequestDTO request,
                        String usuario) {

                ExpedienteClinico expediente = expedienteRepository
                                .findByPaciente_IdPaciente(idPaciente)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Expediente no encontrado"));

                AntecedentePaciente antecedente = antecedenteRepository
                                .findByExpediente_IdExpediente(
                                                expediente.getIdExpediente())
                                .orElse(
                                                AntecedentePaciente.builder()
                                                                .expediente(expediente)
                                                                .build());

                antecedente.setAntecedentesPersonales(
                                request.getAntecedentesPersonales());

                antecedente.setAntecedentesFamiliares(
                                request.getAntecedentesFamiliares());

                antecedente.setAlergias(
                                request.getAlergias());

                antecedente.setCondicionesRelevantes(
                                request.getCondicionesRelevantes());

                antecedente.setUsuarioModificacion(usuario);

                antecedente.setFechaActualizacion(
                                LocalDateTime.now());

                antecedenteRepository.save(antecedente);

                return mapper.toResponse(antecedente);

        }

}