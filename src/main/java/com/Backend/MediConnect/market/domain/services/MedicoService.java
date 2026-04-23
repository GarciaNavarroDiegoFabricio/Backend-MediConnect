package com.Backend.MediConnect.market.domain.services;

import com.Backend.MediConnect.market.domain.dto.CitaResponseDTO;
import com.Backend.MediConnect.market.domain.dto.RecetaDTO;
import com.Backend.MediConnect.market.domain.dto.RecetaResponseDTO;
import com.Backend.MediConnect.market.domain.dto.ReporteResponseDTO;
import com.Backend.MediConnect.market.domain.interfaces.IMedicoService;
import com.Backend.MediConnect.market.web.mapper.EntityMapper;
import com.Backend.MediConnect.market.domain.repository.*;
import com.Backend.MediConnect.market.persistance.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicoService implements IMedicoService {

    private final MedicoRepository medicoRepo;
    private final PacienteRepository pacienteRepo;
    private final ConsultaRepository consultaRepo;
    private final RecetaRepository recetaRepo;
    private final CitaRepository citaRepo;
    private final ReporteRepository reporteRepo;

    public MedicoService(MedicoRepository medicoRepo,
                         PacienteRepository pacienteRepo,
                         ConsultaRepository consultaRepo,
                         RecetaRepository recetaRepo,
                         CitaRepository citaRepo,
                         ReporteRepository reporteRepo) {
        this.medicoRepo = medicoRepo;
        this.pacienteRepo = pacienteRepo;
        this.consultaRepo = consultaRepo;
        this.recetaRepo = recetaRepo;
        this.citaRepo = citaRepo;
        this.reporteRepo = reporteRepo;
    }

    @Override
    @Transactional
    public void cambiarDisponibilidad(String dniMedico, Boolean disponible) {
        Medico medico = medicoRepo.findByDni(dniMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        medico.setDisponible(disponible);
        medicoRepo.save(medico);
    }

    @Override
    @Transactional
    public ReporteResponseDTO generarReporteConsulta(String dniMedico) {
        Medico medico = medicoRepo.findByDni(dniMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        List<Cita> citas = citaRepo.findByMedico(medico);

        long atendidas     = citas.stream().filter(c -> c.getEstado().equals("ATENDIDA")).count();
        long canceladas    = citas.stream().filter(c -> c.getEstado().equals("CANCELADA")).count();
        long reprogramadas = citas.stream().filter(c -> c.getEstado().equals("REPROGRAMADA")).count();
        long pendientes    = citas.stream().filter(c -> c.getEstado().equals("PENDIENTE")).count();

        Reporte reporte = new Reporte();
        reporte.setFechaReporte(LocalTime.now());
        reporte.setCitasAtendidas((int) atendidas);
        reporte.setCitasCanceladas((int) canceladas);
        reporte.setCitasReprogramadas((int) reprogramadas);
        reporte.setCitasPendientes((int) pendientes);

        return EntityMapper.toReporteResponse(reporteRepo.save(reporte));
    }

    @Override
    @Transactional
    public RecetaResponseDTO crearReceta(String dniMedico, RecetaDTO dto) {
        Medico medico = medicoRepo.findByDni(dniMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        Paciente paciente = pacienteRepo.findById(dto.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Consulta consulta = consultaRepo.findById(dto.getIdConsulta())
                .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        Receta receta = new Receta();
        receta.setMedico(medico);
        receta.setPaciente(paciente);
        receta.setConsulta(consulta);
        receta.setPrescripcion(dto.getPrescripcion());
        receta.setFecha(dto.getFecha());

        return EntityMapper.toRecetaResponse(recetaRepo.save(receta));
    }

    @Override
    public List<CitaResponseDTO> consultarReservas(String dniMedico) {
        Medico medico = medicoRepo.findByDni(dniMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        return citaRepo.findByMedicoAndEstado(medico, "PENDIENTE")
                .stream()
                .map(EntityMapper::toCitaResponse)
                .collect(Collectors.toList());
    }
}