package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.request.DetalleRecetaRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.RecetaRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.RecetaResponseDTO;
import com.Backend.MediConnect.clinica.domain.exception.BusinessException;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.repository.IAtencionMedicaRepository;
import com.Backend.MediConnect.clinica.domain.repository.IDetalleRecetaRepository;
import com.Backend.MediConnect.clinica.domain.repository.IRecetaRepository;
import com.Backend.MediConnect.clinica.persistance.entity.AtencionMedica;
import com.Backend.MediConnect.clinica.persistance.entity.DetalleReceta;
import com.Backend.MediConnect.clinica.persistance.entity.Receta;
import com.Backend.MediConnect.clinica.web.mapper.RecetaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RecetaService {

    private final IRecetaRepository recetaRepository;
    private final IDetalleRecetaRepository detalleRecetaRepository;
    private final IAtencionMedicaRepository atencionMedicaRepository;
    private final RecetaMapper recetaMapper;
    private final RecetaPdfService recetaPdfService;
    private final EmailService emailService;

    public RecetaService(IRecetaRepository recetaRepository, IDetalleRecetaRepository detalleRecetaRepository,
                         IAtencionMedicaRepository atencionMedicaRepository, RecetaMapper recetaMapper,
                         RecetaPdfService recetaPdfService, EmailService emailService) {
        this.recetaRepository = recetaRepository;
        this.detalleRecetaRepository = detalleRecetaRepository;
        this.atencionMedicaRepository = atencionMedicaRepository;
        this.recetaMapper = recetaMapper;
        this.recetaPdfService = recetaPdfService;
        this.emailService = emailService;
    }

    @Transactional
    public RecetaResponseDTO generar(Long idAtencion, RecetaRequestDTO request) {
        AtencionMedica atencion = atencionMedicaRepository.findById(idAtencion)
                .orElseThrow(() -> new ResourceNotFoundException("Atención médica no encontrada."));

        if (recetaRepository.findByAtencionMedica_IdAtencion(idAtencion).isPresent()) {
            throw new BusinessException("Ya existe una receta registrada para esta atención.");
        }

        Receta receta = Receta.builder()
                .atencionMedica(atencion)
                .codigoReceta(generarCodigoUnico())
                .observaciones(request.getObservaciones())
                .build();

        receta = recetaRepository.save(receta);

        Receta recetaFinal = receta;
        List<DetalleReceta> detalles = request.getDetalles().stream()
                .map(detalleDto -> construirDetalle(recetaFinal, detalleDto))
                .map(detalleRecetaRepository::save)
                .toList();

        byte[] pdf = recetaPdfService.generarPdf(receta, detalles);

        emailService.enviarReceta(
                atencion.getCita().getPaciente().getPersona().getUsuario().getCorreo(),
                atencion.getCita().getPaciente().getPersona().getNombres(),
                receta.getCodigoReceta(),
                pdf);

        return recetaMapper.toResponse(receta, detalles);
    }

    private DetalleReceta construirDetalle(Receta receta, DetalleRecetaRequestDTO dto) {
        return DetalleReceta.builder()
                .receta(receta)
                .medicamento(dto.getMedicamento())
                .dosis(dto.getDosis())
                .frecuencia(dto.getFrecuencia())
                .duracion(dto.getDuracion())
                .indicaciones(dto.getIndicaciones())
                .build();
    }

    private String generarCodigoUnico() {
        String codigo;
        do {
            codigo = "RX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (recetaRepository.existsByCodigoReceta(codigo));
        return codigo;
    }

    public RecetaResponseDTO consultarPorAtencion(Long idAtencion) {
        Receta receta = recetaRepository.findByAtencionMedica_IdAtencion(idAtencion)
                .orElseThrow(() -> new ResourceNotFoundException("Esta atención no tiene receta registrada."));

        List<DetalleReceta> detalles = detalleRecetaRepository.findByReceta_IdReceta(receta.getIdReceta());
        return recetaMapper.toResponse(receta, detalles);
    }

    public RecetaResponseDTO consultarPorId(Long idReceta) {
        Receta receta = recetaRepository.findById(idReceta)
                .orElseThrow(() -> new ResourceNotFoundException("Receta no encontrada."));

        List<DetalleReceta> detalles = detalleRecetaRepository.findByReceta_IdReceta(idReceta);
        return recetaMapper.toResponse(receta, detalles);
    }

    public List<RecetaResponseDTO> listarPorPaciente(Long idPaciente) {
        return recetaRepository.findByAtencionMedica_Cita_Paciente_IdPaciente(idPaciente).stream()
                .map(receta -> recetaMapper.toResponse(receta,
                        detalleRecetaRepository.findByReceta_IdReceta(receta.getIdReceta())))
                .toList();
    }

    public byte[] descargarPdf(Long idReceta) {
        Receta receta = recetaRepository.findById(idReceta)
                .orElseThrow(() -> new ResourceNotFoundException("Receta no encontrada."));

        List<DetalleReceta> detalles = detalleRecetaRepository.findByReceta_IdReceta(idReceta);
        return recetaPdfService.generarPdf(receta, detalles);
    }
}