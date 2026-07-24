package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.response.*;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.repository.*;
import com.Backend.MediConnect.clinica.persistance.entity.*;
import com.Backend.MediConnect.clinica.web.mapper.ConsultaMapper;
import com.Backend.MediConnect.clinica.web.mapper.HistoriaClinicaMapper;
import com.Backend.MediConnect.clinica.web.mapper.RecetaMapper;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpedienteClinicoService {

    private final IExpedienteClinicoRepository expedienteRepository;
    private final IExpedienteConsultaRepository expedienteConsultaRepository;

    private final ISignoVitalRepository signoVitalRepository;
    private final IDiagnosticoMedicoRepository diagnosticoRepository;
    private final IDetalleAtencionMedicaRepository detalleRepository;

    private final ConsultaMapper consultaMapper;
    private final IRecetaRepository recetaRepository;
    private final IDetalleRecetaRepository detalleRecetaRepository;
    private final RecetaMapper recetaMapper;
    private final IAntecedentePacienteRepository antecedenteRepository;
    private final HistoriaClinicaMapper historiaClinicaMapper;

    public ExpedienteClinicoService(
            IExpedienteClinicoRepository expedienteRepository,
            IExpedienteConsultaRepository expedienteConsultaRepository,
            ISignoVitalRepository signoVitalRepository,
            IDiagnosticoMedicoRepository diagnosticoRepository,
            IDetalleAtencionMedicaRepository detalleRepository,
            ConsultaMapper consultaMapper,
            IRecetaRepository recetaRepository,
            IDetalleRecetaRepository detalleRecetaRepository,
            RecetaMapper recetaMapper,
            IAntecedentePacienteRepository antecedenteRepository,
            HistoriaClinicaMapper historiaClinicaMapper) {

        this.expedienteRepository = expedienteRepository;
        this.expedienteConsultaRepository = expedienteConsultaRepository;
        this.signoVitalRepository = signoVitalRepository;
        this.diagnosticoRepository = diagnosticoRepository;
        this.detalleRepository = detalleRepository;
        this.consultaMapper = consultaMapper;
        this.recetaRepository = recetaRepository;
        this.detalleRecetaRepository = detalleRecetaRepository;
        this.recetaMapper = recetaMapper;
        this.antecedenteRepository = antecedenteRepository;
        this.historiaClinicaMapper = historiaClinicaMapper;
    }

    public ExpedienteClinicoResponseDTO consultarPorPaciente(Long idPaciente) {

        // 1. Buscar expediente del paciente

        ExpedienteClinico expediente = expedienteRepository
                .findByPaciente_IdPaciente(idPaciente)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El paciente no tiene expediente clínico."));

        AntecedentePacienteResponseDTO antecedentes = antecedenteRepository
                .findByExpediente_IdExpediente(
                        expediente.getIdExpediente())
                .map(historiaClinicaMapper::toResponse)
                .orElse(null);

        // 2. Obtener consultas relacionadas

        List<ConsultaResponseDTO> consultas = expedienteConsultaRepository
                .findByExpediente_IdExpediente(
                        expediente.getIdExpediente())
                .stream()
                .map(expConsulta -> {

                    Consulta consulta = expConsulta.getConsulta();
                    RecetaResponseDTO receta = null;

                    Receta recetaEntidad = recetaRepository
                            .findByConsulta_IdConsulta(
                                    consulta.getIdConsulta())
                            .orElse(null);

                    if (recetaEntidad != null) {

                        List<DetalleReceta> detalles = detalleRecetaRepository
                                .findByReceta_IdReceta(
                                        recetaEntidad.getIdReceta());

                        receta = recetaMapper.toResponse(
                                recetaEntidad,
                                detalles);
                    }
                    SignoVital signo = signoVitalRepository
                            .findByConsulta_IdConsulta(
                                    consulta.getIdConsulta())
                            .orElse(null);

                    List<DiagnosticoMedico> diagnosticos = diagnosticoRepository
                            .findByConsulta_IdConsulta(
                                    consulta.getIdConsulta());

                    DetalleAtencionMedica detalle = detalleRepository
                            .findByConsulta_IdConsulta(
                                    consulta.getIdConsulta())
                            .orElse(null);

                    ConsultaResponseDTO response = consultaMapper.toResponse(
                            consulta,
                            signo,
                            diagnosticos,
                            detalle);

                    response.setReceta(receta);

                    return response;
                })
                .toList();

        Paciente paciente = expediente.getPaciente();

        String nombrePaciente = paciente.getPersona().getNombres()
                + " "
                + paciente.getPersona().getApellidoPaterno();

        // 3. Retornar expediente completo

        return ExpedienteClinicoResponseDTO.builder()

                .idExpediente(expediente.getIdExpediente())

                .idPaciente(paciente.getIdPaciente())

                .nombrePaciente(nombrePaciente)

                .fechaCreacion(expediente.getFechaCreacion())

                .estado(expediente.getEstado())

                .antecedentes(antecedentes)

                .consultas(consultas)

                .build();

    }

}