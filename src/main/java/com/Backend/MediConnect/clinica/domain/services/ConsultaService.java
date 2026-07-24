package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.request.DiagnosticoMedicoRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.SignoVitalRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.ConsultaInicioResponseDTO;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.repository.*;
import com.Backend.MediConnect.clinica.persistance.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.Backend.MediConnect.clinica.domain.dto.request.DetalleAtencionRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.DetalleRecetaRequestDTO;

import java.time.LocalDate;
import java.util.List;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ConsultaService {

    private final ICitaRepository citaRepository;
    private final IConsultaRepository consultaRepository;
    private final IExpedienteClinicoRepository expedienteRepository;
    private final IExpedienteConsultaRepository expedienteConsultaRepository;
    private final ISignoVitalRepository signoVitalRepository;
    private final IDiagnosticoMedicoRepository diagnosticoMedicoRepository;
    private final IDetalleAtencionMedicaRepository detalleAtencionRepository;
    private final IRecetaRepository recetaRepository;
    private final IDetalleRecetaRepository detalleRecetaRepository;

    public ConsultaService(
            ICitaRepository citaRepository,
            IConsultaRepository consultaRepository,
            IExpedienteClinicoRepository expedienteRepository,
            IExpedienteConsultaRepository expedienteConsultaRepository,
            ISignoVitalRepository signoVitalRepository,
            IDiagnosticoMedicoRepository diagnosticoMedicoRepository,
            IDetalleAtencionMedicaRepository detalleAtencionRepository,
            IRecetaRepository recetaRepository,
            IDetalleRecetaRepository detalleRecetaRepository) {
        this.citaRepository = citaRepository;
        this.consultaRepository = consultaRepository;
        this.expedienteRepository = expedienteRepository;
        this.expedienteConsultaRepository = expedienteConsultaRepository;
        this.signoVitalRepository = signoVitalRepository;
        this.diagnosticoMedicoRepository = diagnosticoMedicoRepository;
        this.detalleAtencionRepository = detalleAtencionRepository;
        this.recetaRepository = recetaRepository;
        this.detalleRecetaRepository = detalleRecetaRepository;
    }

    @Transactional
    public ConsultaInicioResponseDTO iniciarConsulta(Long idCita) {

        // 1. Buscar cita
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cita no encontrada."));

        // 2. Validar que la cita no tenga una consulta iniciada
        if (consultaRepository.findByCita_IdCita(idCita).isPresent()) {

            throw new IllegalStateException(
                    "La consulta para esta cita ya fue iniciada.");
        }

        // 3. Obtener paciente
        Paciente paciente = cita.getPaciente();

        // 4. Buscar o crear expediente clínico

        ExpedienteClinico expediente = expedienteRepository
                .findByPaciente_IdPaciente(
                        paciente.getIdPaciente())
                .orElseGet(() -> {

                    ExpedienteClinico nuevo = ExpedienteClinico.builder()
                            .paciente(paciente)
                            .estado("ACTIVO")
                            .build();

                    return expedienteRepository.save(nuevo);
                });

        // 5. Crear consulta

        Consulta consulta = Consulta.builder()
                .cita(cita)
                .paciente(paciente)
                .medico(cita.getMedico())
                .estado("EN_CURSO")
                .horaInicio(LocalDateTime.now())
                .build();

        consulta = consultaRepository.save(consulta);

        // 6. Relacionar expediente con consulta

        ExpedienteConsulta expedienteConsulta = ExpedienteConsulta.builder()
                .expediente(expediente)
                .consulta(consulta)
                .build();

        expedienteConsultaRepository.save(expedienteConsulta);

        // 7. Respuesta al frontend

        String nombrePaciente = paciente.getPersona().getNombres()
                + " "
                + paciente.getPersona().getApellidoPaterno();

        return ConsultaInicioResponseDTO.builder()
                .idConsulta(consulta.getIdConsulta())
                .idCita(cita.getIdCita())
                .idPaciente(paciente.getIdPaciente())
                .nombrePaciente(nombrePaciente)
                .idMedico(cita.getMedico().getIdMedico())
                .estado(consulta.getEstado())
                .horaInicio(consulta.getHoraInicio())
                .build();

    }

    @Transactional
    public void registrarSignosVitales(Long idConsulta,
            SignoVitalRequestDTO request) {

        Consulta consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta no encontrada."));

        SignoVital signo = signoVitalRepository
                .findByConsulta_IdConsulta(idConsulta)
                .orElse(SignoVital.builder()
                        .consulta(consulta)
                        .build());

        signo.setPeso(
                request.getPeso() != null
                        ? BigDecimal.valueOf(request.getPeso())
                        : null);
        signo.setTalla(
                request.getTalla() != null
                        ? BigDecimal.valueOf(request.getTalla())
                        : null);
        signo.setPresionArterial(request.getPresionArterial());
        signo.setTemperatura(
                request.getTemperatura() != null
                        ? BigDecimal.valueOf(request.getTemperatura())
                        : null);
        signo.setFrecuenciaCardiaca(request.getFrecuenciaCardiaca());

        signo.setSaturacionOxigeno(
                request.getSaturacionOxigeno());

        signoVitalRepository.save(signo);
    }

    @Transactional
    public void registrarDiagnostico(
            Long idConsulta,
            DiagnosticoMedicoRequestDTO request) {

        Consulta consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta no encontrada."));

        DiagnosticoMedico diagnostico = DiagnosticoMedico.builder()
                .consulta(consulta)
                .descripcionClinica(request.getDescripcionClinica())
                .categoriaDiagnostica(request.getCategoriaDiagnostica())
                .build();

        diagnosticoMedicoRepository.save(diagnostico);
    }

    @Transactional
    public void registrarDetalleAtencion(
            Long idConsulta,
            DetalleAtencionRequestDTO request) {

        Consulta consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta no encontrada."));

        DetalleAtencionMedica detalle = detalleAtencionRepository
                .findByConsulta_IdConsulta(idConsulta)
                .orElse(
                        DetalleAtencionMedica.builder()
                                .consulta(consulta)
                                .build());

        detalle.setTratamiento(request.getTratamiento());
        detalle.setIndicacionesMedicas(request.getIndicacionesMedicas());
        detalle.setObservaciones(request.getObservaciones());
        detalle.setRecomendaciones(request.getRecomendaciones());

        detalleAtencionRepository.save(detalle);
    }

    @Transactional
    public void registrarReceta(
            Long idConsulta,
            List<DetalleRecetaRequestDTO> detallesReceta) {

        Consulta consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta no encontrada."));

        Receta receta = recetaRepository
                .findByConsulta_IdConsulta(idConsulta)
                .orElseGet(() -> {

                    Receta nueva = Receta.builder()
                            .consulta(consulta)
                            .medico(consulta.getMedico())
                            .paciente(consulta.getPaciente())
                            .fecha(LocalDate.now())
                            .build();

                    return recetaRepository.save(nueva);
                });

        detalleRecetaRepository.deleteByReceta_IdReceta(receta.getIdReceta());

        for (DetalleRecetaRequestDTO dto : detallesReceta) {

            DetalleReceta detalle = DetalleReceta.builder()
                    .receta(receta)
                    .medicamento(dto.getMedicamento())
                    .dosis(dto.getDosis())
                    .frecuencia(dto.getFrecuencia())
                    .duracion(dto.getDuracion())
                    .indicaciones(dto.getIndicaciones())
                    .build();

            detalleRecetaRepository.save(detalle);
        }

    }

    @Transactional
    public void finalizarConsulta(Long idConsulta) {

        Consulta consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta no encontrada."));

        if ("FINALIZADA".equals(consulta.getEstado())) {
            throw new IllegalStateException("La consulta ya fue finalizada.");
        }

        if (signoVitalRepository.findByConsulta_IdConsulta(idConsulta).isEmpty()) {
            throw new IllegalStateException("Debe registrar los signos vitales.");
        }

        if (diagnosticoMedicoRepository.findByConsulta_IdConsulta(idConsulta).isEmpty()) {
            throw new IllegalStateException("Debe registrar al menos un diagnóstico.");
        }

        if (detalleAtencionRepository.findByConsulta_IdConsulta(idConsulta).isEmpty()) {
            throw new IllegalStateException("Debe registrar el detalle de atención.");
        }

        consulta.setEstado("FINALIZADA");
        consulta.setHoraFin(LocalDateTime.now());

        consultaRepository.save(consulta);
    }

}