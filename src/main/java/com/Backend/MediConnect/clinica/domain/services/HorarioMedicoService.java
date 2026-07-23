package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.request.HorarioMedicoRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.HorarioMedicoResponseDTO;
import com.Backend.MediConnect.clinica.domain.exception.BusinessException;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.repository.IHorarioMedicoRepository;
import com.Backend.MediConnect.clinica.domain.repository.IMedicoRepository;
import com.Backend.MediConnect.clinica.persistance.entity.HorarioMedico;
import com.Backend.MediConnect.clinica.persistance.entity.Medico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HorarioMedicoService {

    private final IHorarioMedicoRepository horarioRepository;
    private final IMedicoRepository medicoRepository;

    public HorarioMedicoService(IHorarioMedicoRepository horarioRepository, IMedicoRepository medicoRepository) {
        this.horarioRepository = horarioRepository;
        this.medicoRepository = medicoRepository;
    }

    @Transactional
    public HorarioMedicoResponseDTO crear(Long idMedico, HorarioMedicoRequestDTO request, String usuarioCreacion) {
        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado."));

        if (!request.getHoraFin().isAfter(request.getHoraInicio())) {
            throw new BusinessException("La hora de fin debe ser posterior a la hora de inicio.");
        }

        HorarioMedico horario = HorarioMedico.builder()
                .medico(medico)
                .diaSemana(request.getDiaSemana().toUpperCase())
                .horaInicio(request.getHoraInicio())
                .horaFin(request.getHoraFin())
                .estado("ACTIVO")
                .usuarioCreacion(usuarioCreacion)
                .build();

        horario = horarioRepository.save(horario);
        return toResponse(horario);
    }

    @Transactional
    public HorarioMedicoResponseDTO actualizar(Long idHorario, HorarioMedicoRequestDTO request, String usuarioModificacion) {
        HorarioMedico horario = horarioRepository.findById(idHorario)
                .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado."));

        if (!request.getHoraFin().isAfter(request.getHoraInicio())) {
            throw new BusinessException("La hora de fin debe ser posterior a la hora de inicio.");
        }

        horario.setDiaSemana(request.getDiaSemana().toUpperCase());
        horario.setHoraInicio(request.getHoraInicio());
        horario.setHoraFin(request.getHoraFin());
        horario.setUsuarioModificacion(usuarioModificacion);

        horario = horarioRepository.save(horario);
        return toResponse(horario);
    }

    @Transactional
    public void inactivar(Long idHorario, String usuarioModificacion) {
        HorarioMedico horario = horarioRepository.findById(idHorario)
                .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado."));

        horario.setEstado("INACTIVO");
        horario.setUsuarioModificacion(usuarioModificacion);
        horarioRepository.save(horario);
    }

    public List<HorarioMedicoResponseDTO> listarPorMedico(Long idMedico) {
        return horarioRepository.findByMedico_IdMedico(idMedico).stream()
                .map(this::toResponse)
                .toList();
    }

    private HorarioMedicoResponseDTO toResponse(HorarioMedico horario) {
        return HorarioMedicoResponseDTO.builder()
                .idHorario(horario.getIdHorario())
                .idMedico(horario.getMedico().getIdMedico())
                .diaSemana(horario.getDiaSemana())
                .horaInicio(horario.getHoraInicio())
                .horaFin(horario.getHoraFin())
                .estado(horario.getEstado())
                .build();
    }
}