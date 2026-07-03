package com.Backend.MediConnect.clinica.domain.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import com.Backend.MediConnect.clinica.domain.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Backend.MediConnect.clinica.domain.interfaces.IMedicoService;
import com.Backend.MediConnect.clinica.domain.repository.CitaRepository;
import com.Backend.MediConnect.clinica.domain.repository.ConsultaRepository;
import com.Backend.MediConnect.clinica.domain.repository.HorarioRepository;
import com.Backend.MediConnect.clinica.domain.repository.MedicoRepository;
import com.Backend.MediConnect.clinica.domain.repository.PacienteRepository;
import com.Backend.MediConnect.clinica.domain.repository.RecetaRepository;
import com.Backend.MediConnect.clinica.domain.repository.ReporteRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Cita;
import com.Backend.MediConnect.clinica.persistance.entity.Consulta;
import com.Backend.MediConnect.clinica.persistance.entity.Medico;
import com.Backend.MediConnect.clinica.persistance.entity.Paciente;
import com.Backend.MediConnect.clinica.persistance.entity.Receta;
import com.Backend.MediConnect.clinica.persistance.entity.Reporte;
import com.Backend.MediConnect.clinica.web.mapper.CitaMapper;
import com.Backend.MediConnect.clinica.web.mapper.ConsultaMapper;
import com.Backend.MediConnect.clinica.web.mapper.RecetaMapper;
import com.Backend.MediConnect.clinica.web.mapper.ReporteMapper;

@Service
public class MedicoService implements IMedicoService {

        private final MedicoRepository medicoRepo;
        private final PacienteRepository pacienteRepo;
        private final ConsultaRepository consultaRepo;
        private final RecetaRepository recetaRepo;
        private final CitaRepository citaRepo;
        private final ReporteRepository reporteRepo;
        private final HorarioRepository horarioRepo;

        public MedicoService(MedicoRepository medicoRepo,
                        PacienteRepository pacienteRepo,
                        ConsultaRepository consultaRepo,
                        RecetaRepository recetaRepo,
                        CitaRepository citaRepo,
                        ReporteRepository reporteRepo,
                        HorarioRepository horarioRepo) {
                this.medicoRepo = medicoRepo;
                this.pacienteRepo = pacienteRepo;
                this.consultaRepo = consultaRepo;
                this.recetaRepo = recetaRepo;
                this.citaRepo = citaRepo;
                this.reporteRepo = reporteRepo;
                this.horarioRepo = horarioRepo;
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

                long atendidas = citas.stream().filter(c -> c.getEstado().equals("ATENDIDA")).count();
                long canceladas = citas.stream().filter(c -> c.getEstado().equals("CANCELADA")).count();
                long reprogramadas = citas.stream().filter(c -> c.getEstado().equals("REPROGRAMADA")).count();
                long pendientes = citas.stream().filter(c -> c.getEstado().equals("PENDIENTE")).count();

                Reporte reporte = new Reporte();
                reporte.setFechaReporte(LocalTime.now());
                reporte.setCitasAtendidas((int) atendidas);
                reporte.setCitasCanceladas((int) canceladas);
                reporte.setCitasReprogramadas((int) reprogramadas);
                reporte.setCitasPendientes((int) pendientes);

                return ReporteMapper.toResponse(reporteRepo.save(reporte));
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

                return RecetaMapper.toResponse(recetaRepo.save(receta));
        }

        @Override
        public List<CitaResponseDTO> consultarReservas(String dniMedico) {
                Medico medico = medicoRepo.findByDni(dniMedico)
                                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
                return citaRepo
                                .findByMedicoAndEstadoOrderByFechaAscHoraAsc(medico, "PENDIENTE")
                                .stream()
                                .map(CitaMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public List<PacienteBusquedaDTO> buscarPacientes(String termino) {
                return List.of();
        }

        @Transactional
        @Override
        public void ponerEnEspera(Integer idCita, String dniMedico) {

                Cita cita = citaRepo.findById(idCita)
                                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

                Medico medico = cita.getMedico();

                if (!cita.getEstado().equals("PENDIENTE"))
                        throw new RuntimeException(
                                        "Solo una cita pendiente puede ponerse en espera.");

                if (LocalDate.now().isBefore(cita.getFecha())) {
                        throw new RuntimeException("La cita aún no corresponde al día de hoy.");
                }

                if (LocalDate.now().isAfter(cita.getFecha())) {
                        throw new RuntimeException("La fecha de la cita ya pasó.");
                }

                if (LocalTime.now().isBefore(cita.getHora())) {
                        throw new RuntimeException("Todavía no es la hora programada para la cita.");
                }

                Medico medicoLogueado = medicoRepo.findByDni(dniMedico)
                                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

                if (!cita.getMedico().getIdMedico().equals(medicoLogueado.getIdMedico())) {
                        throw new RuntimeException("No tiene permisos para modificar esta cita.");
                }

                medico.setEstado("EN_ESPERA");

                cita.setEstado("EN_ESPERA");

                medicoRepo.save(medico);

                citaRepo.save(cita);

        }

        @Transactional
        @Override
        public ConsultaResponseDTO comenzarConsulta(Integer idCita, String dniMedico) {

                Cita cita = citaRepo.findById(idCita)
                                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

                if (consultaRepo.existsByCita(cita)) {
                        throw new RuntimeException("La consulta ya fue iniciada.");
                }

                if ("CANCELADA".equals(cita.getEstado())) {
                        throw new RuntimeException("No se puede iniciar una cita cancelada.");
                }

                if ("ATENDIDA".equals(cita.getEstado())) {
                        throw new RuntimeException("La cita ya fue atendida.");
                }

                if (!cita.getFecha().equals(LocalDate.now())) {
                        throw new RuntimeException("Solo se pueden iniciar consultas del día de hoy.");
                }

                Medico medicoLogueado = medicoRepo.findByDni(dniMedico)
                                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

                if (!cita.getMedico().getIdMedico().equals(medicoLogueado.getIdMedico())) {
                        throw new RuntimeException("No tiene permisos para modificar esta cita.");
                }

                if ("EN_CONSULTA".equals(cita.getMedico().getEstado())) {
                        throw new RuntimeException("El médico ya tiene una consulta en curso.");
                }

                Consulta consulta = new Consulta();

                consulta.setCita(cita);

                consulta.setPaciente(cita.getPaciente());

                consulta.setMedico(cita.getMedico());

                consulta.setHoraInicio(LocalDateTime.now());

                consulta.setEstado("EN_CURSO");

                consulta = consultaRepo.save(consulta);

                cita.setEstado("EN_CURSO");

                citaRepo.save(cita);

                Medico medico = cita.getMedico();

                medico.setEstado("EN_CONSULTA");

                medicoRepo.save(medico);

                return ConsultaMapper.toResponse(consulta);

        }

        @Transactional
        @Override
        public ConsultaResponseDTO terminarConsulta(Integer idConsulta, String dniMedico) {

                Consulta consulta = consultaRepo.findById(idConsulta)
                                .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

                if (!"EN_CURSO".equals(consulta.getEstado())) {
                        throw new RuntimeException("La consulta no está en curso.");
                }

                if (consulta.getHoraInicio() == null) {
                        throw new RuntimeException("La consulta aún no ha iniciado.");
                }

                if (consulta.getHoraFin() != null) {
                        throw new RuntimeException("La consulta ya fue finalizada.");
                }

                Medico medicoLogueado = medicoRepo.findByDni(dniMedico)
                                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

                consulta.setHoraFin(LocalDateTime.now());

                consulta.setEstado("FINALIZADA");

                consultaRepo.save(consulta);

                Cita cita = consulta.getCita();

                if (!cita.getMedico().getIdMedico().equals(medicoLogueado.getIdMedico())) {
                        throw new RuntimeException("No tiene permisos para modificar esta cita.");
                }

                cita.setEstado("ATENDIDA");

                citaRepo.save(cita);

                Medico medico = consulta.getMedico();

                medico.setEstado("DISPONIBLE");

                medicoRepo.save(medico);

                return ConsultaMapper.toResponse(consulta);

        }
}