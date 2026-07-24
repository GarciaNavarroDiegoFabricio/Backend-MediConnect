package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.request.DetalleRecetaRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.RecetaRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.RecetaResponseDTO;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.repository.*;
import com.Backend.MediConnect.clinica.persistance.entity.*;
import com.Backend.MediConnect.clinica.web.mapper.RecetaMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecetaService {

    private final IRecetaRepository recetaRepository;
    private final IDetalleRecetaRepository detalleRecetaRepository;
    private final IConsultaRepository consultaRepository;
    private final RecetaMapper recetaMapper;
    private final RecetaPdfService recetaPdfService;

    public RecetaService(
            IRecetaRepository recetaRepository,
            IDetalleRecetaRepository detalleRecetaRepository,
            IConsultaRepository consultaRepository,
            RecetaMapper recetaMapper,
            RecetaPdfService recetaPdfService) {

        this.recetaRepository = recetaRepository;
        this.detalleRecetaRepository = detalleRecetaRepository;
        this.consultaRepository = consultaRepository;
        this.recetaMapper = recetaMapper;
        this.recetaPdfService = recetaPdfService;

    }

    @Transactional
    public RecetaResponseDTO generar(
            Long idConsulta,
            RecetaRequestDTO request) {

        Consulta consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Consulta no encontrada."));

        Receta receta = Receta.builder()
                .consulta(consulta)
                .medico(consulta.getMedico())
                .paciente(consulta.getPaciente())
                .fecha(LocalDate.now())
                .prescripcion(request.getObservaciones())
                .build();

        recetaRepository.save(receta);

        List<DetalleReceta> detalles = request.getDetalles()
                .stream()
                .map(dto -> DetalleReceta.builder()
                        .receta(receta)
                        .medicamento(dto.getMedicamento())
                        .dosis(dto.getDosis())
                        .frecuencia(dto.getFrecuencia())
                        .duracion(dto.getDuracion())
                        .indicaciones(dto.getIndicaciones())
                        .build())
                .map(detalleRecetaRepository::save)
                .toList();

        return recetaMapper.toResponse(receta, detalles);

    }

    public byte[] descargarPdf(Long idReceta) {

        Receta receta = recetaRepository.findById(idReceta)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Receta no encontrada."));

        List<DetalleReceta> detalles = detalleRecetaRepository
                .findByReceta_IdReceta(idReceta);

        return recetaPdfService.generarPdf(receta, detalles);
    }

    public RecetaResponseDTO consultarPorConsulta(Long idConsulta) {

        Receta receta = recetaRepository.findByConsulta_IdConsulta(idConsulta)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "No existe receta para esta consulta."));

        List<DetalleReceta> detalles = detalleRecetaRepository
                .findByReceta_IdReceta(receta.getIdReceta());

        return recetaMapper.toResponse(receta, detalles);
    }

    public List<RecetaResponseDTO> listarPorPaciente(Long idPaciente) {

        return recetaRepository.findByPaciente_IdPaciente(idPaciente)
                .stream()
                .map(receta -> {

                    List<DetalleReceta> detalles = detalleRecetaRepository
                            .findByReceta_IdReceta(receta.getIdReceta());

                    return recetaMapper.toResponse(receta, detalles);

                })
                .toList();
    }

    public RecetaResponseDTO consultarPorId(Long idReceta) {

        Receta receta = recetaRepository.findById(idReceta)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Receta no encontrada."));

        List<DetalleReceta> detalles = detalleRecetaRepository
                .findByReceta_IdReceta(idReceta);

        return recetaMapper.toResponse(receta, detalles);
    }

}