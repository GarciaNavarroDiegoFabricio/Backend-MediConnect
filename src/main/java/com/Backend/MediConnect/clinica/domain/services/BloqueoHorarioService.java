package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.request.BloqueoHorarioRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.BloqueoHorarioResponseDTO;
import com.Backend.MediConnect.clinica.domain.exception.BusinessException;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.repository.IBloqueoHorarioRepository;
import com.Backend.MediConnect.clinica.domain.repository.IMedicoRepository;
import com.Backend.MediConnect.clinica.persistance.entity.BloqueoHorario;
import com.Backend.MediConnect.clinica.persistance.entity.Medico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BloqueoHorarioService {

    private final IBloqueoHorarioRepository bloqueoRepository;
    private final IMedicoRepository medicoRepository;

    public BloqueoHorarioService(IBloqueoHorarioRepository bloqueoRepository, IMedicoRepository medicoRepository) {
        this.bloqueoRepository = bloqueoRepository;
        this.medicoRepository = medicoRepository;
    }

    @Transactional
    public BloqueoHorarioResponseDTO crear(Long idMedico, BloqueoHorarioRequestDTO request, String usuarioCreacion) {
        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado."));

        if (request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new BusinessException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        BloqueoHorario bloqueo = BloqueoHorario.builder()
                .medico(medico)
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .motivo(request.getMotivo())
                .tipo(request.getTipo().toUpperCase())
                .usuarioCreacion(usuarioCreacion)
                .build();

        bloqueo = bloqueoRepository.save(bloqueo);
        return toResponse(bloqueo);
    }

    @Transactional
    public void eliminar(Long idBloqueo) {
        BloqueoHorario bloqueo = bloqueoRepository.findById(idBloqueo)
                .orElseThrow(() -> new ResourceNotFoundException("Bloqueo no encontrado."));

        bloqueoRepository.delete(bloqueo);
    }

    public List<BloqueoHorarioResponseDTO> listarPorMedico(Long idMedico) {
        return bloqueoRepository.findByMedico_IdMedico(idMedico).stream()
                .map(this::toResponse)
                .toList();
    }

    private BloqueoHorarioResponseDTO toResponse(BloqueoHorario bloqueo) {
        return BloqueoHorarioResponseDTO.builder()
                .idBloqueo(bloqueo.getIdBloqueo())
                .idMedico(bloqueo.getMedico().getIdMedico())
                .fechaInicio(bloqueo.getFechaInicio())
                .fechaFin(bloqueo.getFechaFin())
                .motivo(bloqueo.getMotivo())
                .tipo(bloqueo.getTipo())
                .build();
    }
}