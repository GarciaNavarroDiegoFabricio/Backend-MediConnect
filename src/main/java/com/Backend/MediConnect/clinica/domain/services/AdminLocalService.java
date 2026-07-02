package com.Backend.MediConnect.clinica.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; //  AGREGADO PARA EL RF5

import com.Backend.MediConnect.clinica.domain.dto.BloquearHorarioDTO;
import com.Backend.MediConnect.clinica.domain.dto.EditarHorarioDTO;
import com.Backend.MediConnect.clinica.domain.dto.HorarioDTO;
import com.Backend.MediConnect.clinica.domain.dto.HorarioResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.MedicoResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.ReprogramarHorarioDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.IAdminLocalService;
import com.Backend.MediConnect.clinica.domain.repository.HorarioRepository;
import com.Backend.MediConnect.clinica.domain.repository.MedicoRepository;
import com.Backend.MediConnect.clinica.domain.repository.SedeRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Horario;
import com.Backend.MediConnect.clinica.persistance.entity.Medico;
import com.Backend.MediConnect.clinica.web.mapper.HorarioMapper;
import com.Backend.MediConnect.clinica.web.mapper.MedicoMapper;

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

        Horario horarioGuardado = horarioRepo.save(horario);
        
        verificarYActualizarDisponibilidadMedico(medico.getIdMedico());

        return HorarioMapper.toResponse(horarioGuardado);
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

        Horario horarioGuardado = horarioRepo.save(horario);
        
        verificarYActualizarDisponibilidadMedico(horarioGuardado.getMedico().getIdMedico());

        return HorarioMapper.toResponse(horarioGuardado);
    }

    @Override
    @Transactional
    public void cancelarHorario(Integer idHorario) {
        Horario horario = horarioRepo.findById(idHorario)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        horario.setEstado("CANCELADO");
        horarioRepo.save(horario);
        
        verificarYActualizarDisponibilidadMedico(horario.getMedico().getIdMedico());
    }

    @Override
    @Transactional
    public void bloquearHorario(Integer idHorario, BloquearHorarioDTO dto) {
        Horario horario = horarioRepo.findById(idHorario)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        
        horario.setEstado("BLOQUEADO");
        horario.setMotivo(dto.getMotivo() != null ? dto.getMotivo().toUpperCase() : "NO ESPECIFICADO");
        horarioRepo.save(horario);
        
        verificarYActualizarDisponibilidadMedico(horario.getMedico().getIdMedico());
    }

    @Override
    public List<MedicoResponseDTO> gestionarMedico(Integer idSede) {
        if (!sedeRepo.existsById(idSede))
            throw new RuntimeException("Sede no encontrada");
        return medicoRepo.findBySedesIdSede(idSede)
                .stream()
                .map(MedicoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cambiarEstadoMedico(Integer idMedico, String nuevoEstado) {
        Medico medico = medicoRepo.findById(idMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        
        medico.setEstado(nuevoEstado.toUpperCase());
        medicoRepo.save(medico);
    }

    @Override
    @Transactional
    public HorarioResponseDTO actualizarHorario(Integer idHorario, EditarHorarioDTO dto) {
        Horario horario = horarioRepo.findById(idHorario)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());
        horario.setIntervaloMinutos(dto.getIntervaloMinutos());

        Horario horarioGuardado = horarioRepo.save(horario);
        
        verificarYActualizarDisponibilidadMedico(horarioGuardado.getMedico().getIdMedico());

        return HorarioMapper.toResponse(horarioGuardado);
    }

    @Override
    @Transactional
    public void inactivarHorario(Integer idHorario) {
        Horario horario = horarioRepo.findById(idHorario)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        
        horario.setEstado("INACTIVO");
        horarioRepo.save(horario);
        
        verificarYActualizarDisponibilidadMedico(horario.getMedico().getIdMedico());
    }

    // AUTOMATIZACIÓN EN TIEMPO REAL: REQUERIDO INTEGRANTE 3 - RF2
    @Override
    @Transactional
    public void verificarYActualizarDisponibilidadMedico(Integer idMedico) {
        Medico medico = medicoRepo.findById(idMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        boolean tieneHorariosActivos = medico.getHorarios().stream()
                .anyMatch(h -> "ACTIVO".equalsIgnoreCase(h.getEstado()) || "REPROGRAMADO".equalsIgnoreCase(h.getEstado()));

        if (!tieneHorariosActivos) {
            medico.setDisponible(false);
        } else {
            medico.setDisponible(true);
        }

        medicoRepo.save(medico);
    }

    // =========================================================================
    // AUTOMATIZACIÓN CRON JOB: REQUERIDO INTEGRANTE 3 - RF5
    // Restablece automáticamente la disponibilidad al inicio de la jornada laboral.
    // Se ejecuta de manera interna todos los días a las 06:00 AM.
    // =========================================================================
   @Scheduled(cron = "0 0 6 * * *")
    @Transactional
    public void restablecerDisponibilidadDiaria() {
        // Obtenemos únicamente los médicos que se encuentran como "no disponibles"
        List<Medico> medicosApagados = medicoRepo.findByDisponibleFalse();
        
        for (Medico medico : medicosApagados) {
            // Re-evalúa el estado de sus jornadas actuales para reactivarlo si tiene turnos hábiles
            verificarYActualizarDisponibilidadMedico(medico.getIdMedico());
        }
        System.out.println("Proceso automático RF5 completado con éxito: Estados diarios de disponibilidad reiniciados.");
    }
}