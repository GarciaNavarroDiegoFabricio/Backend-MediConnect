package com.Backend.MediConnect.market.domain.services;

import com.Backend.MediConnect.market.domain.dto.HorarioDTO;
import com.Backend.MediConnect.market.domain.dto.HorarioResponseDTO;
import com.Backend.MediConnect.market.domain.dto.MedicoResponseDTO;
import com.Backend.MediConnect.market.domain.dto.ReprogramarHorarioDTO;
import com.Backend.MediConnect.market.domain.interfaces.IAdminLocalService;
import com.Backend.MediConnect.market.web.mapper.EntityMapper;
import com.Backend.MediConnect.market.domain.repository.*;
import com.Backend.MediConnect.market.persistance.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminLocalService implements IAdminLocalService {

    private final HorarioRepository horarioRepo;
    private final MedicoRepository medicoRepo;
    private final SedeRepository sedeRepo;

    public AdminLocalService(HorarioRepository horarioRepo,
                             MedicoRepository medicoRepo,
                             SedeRepository sedeRepo) {
        this.horarioRepo = horarioRepo;
        this.medicoRepo = medicoRepo;
        this.sedeRepo = sedeRepo;
    }

    @Override
    @Transactional
    public HorarioResponseDTO crearHorario(HorarioDTO dto) {
        Medico medico = medicoRepo.findById(dto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        Horario horario = new Horario();
        horario.setMedico(medico);
        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());
        horario.setIntervaloMinutos(dto.getIntervaloMinutos());
        horario.setEstado("ACTIVO");

        return EntityMapper.toHorarioResponse(horarioRepo.save(horario));
    }

    @Override
    @Transactional
    public HorarioResponseDTO reprogramarHorario(Integer idHorario, ReprogramarHorarioDTO dto) {
        Horario horario = horarioRepo.findById(idHorario)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());
        horario.setIntervaloMinutos(dto.getIntervaloMinutos());
        horario.setEstado("REPROGRAMADO");

        return EntityMapper.toHorarioResponse(horarioRepo.save(horario));
    }

    @Override
    @Transactional
    public void cancelarHorario(Integer idHorario) {
        Horario horario = horarioRepo.findById(idHorario)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        horario.setEstado("CANCELADO");
        horarioRepo.save(horario);
    }

    @Override
    @Transactional
    public void bloquearHorario(Integer idHorario) {
        Horario horario = horarioRepo.findById(idHorario)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        horario.setEstado("BLOQUEADO");
        horarioRepo.save(horario);
    }

    @Override
    public List<MedicoResponseDTO> gestionarMedico(Integer idSede) {
        if (!sedeRepo.existsById(idSede))
            throw new RuntimeException("Sede no encontrada");
        return medicoRepo.findBySedesIdSede(idSede)
                .stream()
                .map(EntityMapper::toMedicoResponse)
                .collect(Collectors.toList());
    }
}