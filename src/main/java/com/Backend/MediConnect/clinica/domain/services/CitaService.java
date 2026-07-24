package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.request.CitaCancelarRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.CitaRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.CitaReprogramarRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.CitaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.HistorialCitaResponseDTO;
import com.Backend.MediConnect.clinica.domain.exception.BusinessException;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.repository.ICitaRepository;
import com.Backend.MediConnect.clinica.domain.repository.IHistorialCitaRepository;
import com.Backend.MediConnect.clinica.domain.repository.IMedicoRepository;
import com.Backend.MediConnect.clinica.domain.repository.IPacienteRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Cita;
import com.Backend.MediConnect.clinica.persistance.entity.HistorialCita;
import com.Backend.MediConnect.clinica.persistance.entity.Medico;
import com.Backend.MediConnect.clinica.persistance.entity.Paciente;
import com.Backend.MediConnect.clinica.web.mapper.CitaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class CitaService {

    private static final Set<String> ESTADOS_ACTIVOS = Set.of(
            "PENDIENTE_CONFIRMACION", "CONFIRMADA", "REPROGRAMADA");

    private final ICitaRepository citaRepository;
    private final IHistorialCitaRepository historialCitaRepository;
    private final IPacienteRepository pacienteRepository;
    private final IMedicoRepository medicoRepository;
    private final CitaMapper citaMapper;
    private final EmailService emailService;

    public CitaService(ICitaRepository citaRepository, IHistorialCitaRepository historialCitaRepository,
            IPacienteRepository pacienteRepository, IMedicoRepository medicoRepository,
            CitaMapper citaMapper, EmailService emailService) {
        this.citaRepository = citaRepository;
        this.historialCitaRepository = historialCitaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.citaMapper = citaMapper;
        this.emailService = emailService;
    }

    @Transactional
    public CitaResponseDTO reservar(Long idUsuarioPaciente, CitaRequestDTO request, String usuarioCreacion) {
        Paciente paciente = pacienteRepository.findByPersona_Usuario_IdUsuario(idUsuarioPaciente)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado."));

        Medico medico = medicoRepository.findById(request.getIdMedico())
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado."));

        if (!"ACTIVO".equals(medico.getEstado()) || !Boolean.TRUE.equals(medico.getDisponible())) {
            throw new BusinessException("El médico no está disponible actualmente.");
        }

        if (!request.getHoraInicio().isBefore(request.getHoraFin())) {
            throw new BusinessException("La hora de inicio debe ser anterior a la hora de fin.");
        }

        if ("VIRTUAL".equalsIgnoreCase(request.getModalidad())) {
            validarModalidad(request.getModalidad());
        } else if (!"PRESENCIAL".equalsIgnoreCase(request.getModalidad())) {
            throw new BusinessException("La modalidad debe ser PRESENCIAL o VIRTUAL.");
        }

        validarDisponibilidadHorario(medico.getIdMedico(), request.getFechaCita(),
                request.getHoraInicio(), request.getHoraFin(), null);

        Cita cita = Cita.builder()
                .paciente(paciente)
                .medico(medico)
                .fechaCita(request.getFechaCita())
                .horaInicio(request.getHoraInicio())
                .horaFin(request.getHoraFin())
                .modalidad(request.getModalidad().toUpperCase())
                .enlaceVideollamada("VIRTUAL".equalsIgnoreCase(request.getModalidad())
                        ? generarEnlaceVideollamada()
                        : null)
                .motivoConsulta(request.getMotivoConsulta())
                .idPago(request.getIdPago())
                .estado("CONFIRMADA")
                .usuarioCreacion(usuarioCreacion)
                .build();

        cita = citaRepository.save(cita);

        registrarHistorial(cita, null, "CONFIRMADA", "Reserva confirmada tras pago.", usuarioCreacion);

        emailService.enviarConfirmacionCita(
                paciente.getPersona().getUsuario().getCorreo(),
                paciente.getPersona().getNombres(),
                cita);

        return citaMapper.toResponse(cita);
    }

    private void validarModalidad(String modalidad) {
        if (modalidad == null || modalidad.isBlank()) {
            throw new BusinessException("La modalidad es obligatoria.");
        }
    }

    private String generarEnlaceVideollamada() {
        return "https://meet.mediconnect.com/" + UUID.randomUUID();
    }

    private void validarDisponibilidadHorario(Long idMedico, LocalDate fecha, LocalTime horaInicio,
            LocalTime horaFin, Long idCitaExcluir) {
        List<Cita> citasDelDia = citaRepository.findByMedico_IdMedicoAndFechaCita(idMedico, fecha);

        boolean hayCruce = citasDelDia.stream()
                .filter(c -> idCitaExcluir == null || !c.getIdCita().equals(idCitaExcluir))
                .filter(c -> ESTADOS_ACTIVOS.contains(c.getEstado()))
                .anyMatch(c -> horaInicio.isBefore(c.getHoraFin()) && horaFin.isAfter(c.getHoraInicio()));

        if (hayCruce) {
            throw new BusinessException("El médico ya tiene una cita programada en ese horario.");
        }
    }

    @Transactional
    public CitaResponseDTO reprogramar(Long idCita, CitaReprogramarRequestDTO request, String usuarioModificacion) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada."));

        if (!ESTADOS_ACTIVOS.contains(cita.getEstado())) {
            throw new BusinessException("Solo se pueden reprogramar citas activas.");
        }

        if (!request.getNuevaHoraInicio().isBefore(request.getNuevaHoraFin())) {
            throw new BusinessException("La hora de inicio debe ser anterior a la hora de fin.");
        }

        validarDisponibilidadHorario(cita.getMedico().getIdMedico(), request.getNuevaFecha(),
                request.getNuevaHoraInicio(), request.getNuevaHoraFin(), cita.getIdCita());

        String estadoAnterior = cita.getEstado();

        cita.setFechaCita(request.getNuevaFecha());
        cita.setHoraInicio(request.getNuevaHoraInicio());
        cita.setHoraFin(request.getNuevaHoraFin());
        cita.setEstado("REPROGRAMADA");
        cita.setUsuarioModificacion(usuarioModificacion);

        cita = citaRepository.save(cita);

        registrarHistorial(cita, estadoAnterior, "REPROGRAMADA", request.getMotivo(), usuarioModificacion);

        emailService.enviarReprogramacionCita(
                cita.getPaciente().getPersona().getUsuario().getCorreo(),
                cita.getPaciente().getPersona().getNombres(),
                cita);

        return citaMapper.toResponse(cita);
    }

    @Transactional
    public void cancelar(Long idCita, CitaCancelarRequestDTO request, String usuarioModificacion) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada."));

        if (!ESTADOS_ACTIVOS.contains(cita.getEstado())) {
            throw new BusinessException("Solo se pueden cancelar citas activas.");
        }

        String estadoAnterior = cita.getEstado();

        cita.setEstado("CANCELADA");
        cita.setUsuarioModificacion(usuarioModificacion);
        citaRepository.save(cita);

        registrarHistorial(cita, estadoAnterior, "CANCELADA", request.getMotivo(), usuarioModificacion);

        emailService.enviarCancelacionCita(
                cita.getPaciente().getPersona().getUsuario().getCorreo(),
                cita.getPaciente().getPersona().getNombres(),
                cita, request.getMotivo());
    }

    @Transactional
    public CitaResponseDTO marcarComoAtendida(Long idCita, String usuarioModificacion) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada."));

        if (!ESTADOS_ACTIVOS.contains(cita.getEstado())) {
            throw new BusinessException("Solo se pueden marcar como atendidas las citas activas.");
        }

        String estadoAnterior = cita.getEstado();
        cita.setEstado("ATENDIDA");
        cita.setUsuarioModificacion(usuarioModificacion);
        cita = citaRepository.save(cita);

        registrarHistorial(cita, estadoAnterior, "ATENDIDA", "Cita atendida.", usuarioModificacion);

        return citaMapper.toResponse(cita);
    }

    @Transactional
    public CitaResponseDTO marcarComoNoAsistio(Long idCita, String usuarioModificacion) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada."));

        if (!ESTADOS_ACTIVOS.contains(cita.getEstado())) {
            throw new BusinessException("Solo se pueden marcar como no asistió las citas activas.");
        }

        String estadoAnterior = cita.getEstado();
        cita.setEstado("NO_ASISTIO");
        cita.setUsuarioModificacion(usuarioModificacion);
        cita = citaRepository.save(cita);

        registrarHistorial(cita, estadoAnterior, "NO_ASISTIO", "Paciente no asistió.", usuarioModificacion);

        return citaMapper.toResponse(cita);
    }

    private void registrarHistorial(Cita cita, String estadoAnterior, String estadoNuevo,
            String motivo, String usuarioCambio) {
        HistorialCita historial = HistorialCita.builder()
                .cita(cita)
                .estadoAnterior(estadoAnterior)
                .estadoNuevo(estadoNuevo)
                .motivoCambio(motivo)
                .usuarioCambio(usuarioCambio)
                .build();
        historialCitaRepository.save(historial);
    }

    public CitaResponseDTO consultarPorId(Long idCita) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada."));
        return citaMapper.toResponse(cita);
    }

    public List<HistorialCitaResponseDTO> consultarHistorial(Long idCita) {
        if (!citaRepository.existsById(idCita)) {
            throw new ResourceNotFoundException("Cita no encontrada.");
        }
        return historialCitaRepository.findByCita_IdCitaOrderByFechaCambioDesc(idCita).stream()
                .map(citaMapper::toHistorialResponse)
                .toList();
    }

    public List<CitaResponseDTO> listarPorPaciente(Long idUsuarioPaciente) {
        return citaRepository.findByPaciente_Persona_Usuario_IdUsuario(idUsuarioPaciente).stream()
                .map(citaMapper::toResponse)
                .toList();
    }

    public List<CitaResponseDTO> listarPorMedico(Long idMedico) {
        return citaRepository.findByMedico_IdMedico(idMedico).stream()
                .map(citaMapper::toResponse)
                .toList();
    }

    public List<CitaResponseDTO> listarPorEstado(String estado) {
        return citaRepository.findByEstado(estado).stream()
                .map(citaMapper::toResponse)
                .toList();
    }

    public List<CitaResponseDTO> listarTodas() {
        return citaRepository.findAll().stream()
                .map(citaMapper::toResponse)
                .toList();
    }

    public List<CitaResponseDTO> listarPorMedicoUsuario(Long idUsuario) {

        Medico medico = medicoRepository
                .findByPersona_Usuario_IdUsuario(idUsuario)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Médico no encontrado"));

        return listarPorMedico(medico.getIdMedico());

    }
}