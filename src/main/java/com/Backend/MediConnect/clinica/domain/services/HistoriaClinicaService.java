package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.request.*;
import com.Backend.MediConnect.clinica.domain.dto.response.*;
import com.Backend.MediConnect.clinica.domain.exception.BusinessException;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.repository.*;
import com.Backend.MediConnect.clinica.persistance.entity.*;
import com.Backend.MediConnect.clinica.web.mapper.HistoriaClinicaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class HistoriaClinicaService {

    private final IHistoriaClinicaRepository historiaClinicaRepository;
    private final IAtencionMedicaRepository atencionMedicaRepository;
    private final IAntecedenteClinicoRepository antecedenteClinicoRepository;
    private final ISignoVitalRepository signoVitalRepository;
    private final IDiagnosticoRepository diagnosticoRepository;
    private final ITratamientoRepository tratamientoRepository;
    private final IDocumentoClinicoRepository documentoClinicoRepository;
    private final ICitaRepository citaRepository;
    private final IPacienteRepository pacienteRepository;
    private final HistoriaClinicaMapper mapper;
    private final DocumentoClinicoStorageService documentoClinicoStorageService;
    private final AtencionMedicaPdfService atencionMedicaPdfService;
    private final EmailService emailService;

    public HistoriaClinicaService(IHistoriaClinicaRepository historiaClinicaRepository,
                                  IAtencionMedicaRepository atencionMedicaRepository,
                                  IAntecedenteClinicoRepository antecedenteClinicoRepository,
                                  ISignoVitalRepository signoVitalRepository,
                                  IDiagnosticoRepository diagnosticoRepository,
                                  ITratamientoRepository tratamientoRepository,
                                  IDocumentoClinicoRepository documentoClinicoRepository,
                                  ICitaRepository citaRepository,
                                  IPacienteRepository pacienteRepository,
                                  HistoriaClinicaMapper mapper,
                                  DocumentoClinicoStorageService documentoClinicoStorageService,
                                  AtencionMedicaPdfService atencionMedicaPdfService,
                                  EmailService emailService) {
        this.historiaClinicaRepository = historiaClinicaRepository;
        this.atencionMedicaRepository = atencionMedicaRepository;
        this.antecedenteClinicoRepository = antecedenteClinicoRepository;
        this.signoVitalRepository = signoVitalRepository;
        this.diagnosticoRepository = diagnosticoRepository;
        this.tratamientoRepository = tratamientoRepository;
        this.documentoClinicoRepository = documentoClinicoRepository;
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
        this.mapper = mapper;
        this.documentoClinicoStorageService = documentoClinicoStorageService;
        this.atencionMedicaPdfService = atencionMedicaPdfService;
        this.emailService = emailService;
    }

    @Transactional
    public HistoriaClinica obtenerOCrear(Paciente paciente) {
        return historiaClinicaRepository.findByPaciente_IdPaciente(paciente.getIdPaciente())
                .orElseGet(() -> historiaClinicaRepository.save(
                        HistoriaClinica.builder().paciente(paciente).build()));
    }

    @Transactional
    public AtencionMedicaResponseDTO iniciarAtencion(Long idCita, String usuarioCreacion) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada."));

        if (atencionMedicaRepository.findByCita_IdCita(idCita).isPresent()) {
            throw new BusinessException("Ya existe una atención registrada para esta cita.");
        }

        HistoriaClinica historia = obtenerOCrear(cita.getPaciente());

        AtencionMedica atencion = AtencionMedica.builder()
                .cita(cita)
                .historiaClinica(historia)
                .motivoConsulta(cita.getMotivoConsulta())
                .usuarioCreacion(usuarioCreacion)
                .build();

        atencion = atencionMedicaRepository.save(atencion);

        return mapper.toResponse(atencion, null, List.of(), List.of());
    }

    @Transactional
    public void registrarSignoVital(Long idAtencion, SignoVitalRequestDTO request) {
        AtencionMedica atencion = obtenerAtencionValidada(idAtencion);

        SignoVital signo = signoVitalRepository.findByAtencionMedica_IdAtencion(idAtencion)
                .orElse(SignoVital.builder().atencionMedica(atencion).build());

        signo.setPresionArterial(request.getPresionArterial());
        signo.setFrecuenciaCardiaca(request.getFrecuenciaCardiaca());
        signo.setFrecuenciaRespiratoria(request.getFrecuenciaRespiratoria());
        signo.setTemperatura(request.getTemperatura());
        signo.setSaturacionOxigeno(request.getSaturacionOxigeno());
        signo.setPeso(request.getPeso());
        signo.setTalla(request.getTalla());

        signoVitalRepository.save(signo);
    }

    @Transactional
    public DiagnosticoResponseDTO registrarDiagnostico(Long idAtencion, DiagnosticoRequestDTO request) {
        AtencionMedica atencion = obtenerAtencionValidada(idAtencion);

        Diagnostico diagnostico = Diagnostico.builder()
                .atencionMedica(atencion)
                .codigoCie10(request.getCodigoCie10())
                .descripcion(request.getDescripcion())
                .tipo(request.getTipo())
                .build();

        diagnostico = diagnosticoRepository.save(diagnostico);
        return mapper.toResponse(diagnostico);
    }

    @Transactional
    public TratamientoResponseDTO registrarTratamiento(Long idAtencion, TratamientoRequestDTO request) {
        AtencionMedica atencion = obtenerAtencionValidada(idAtencion);

        Tratamiento tratamiento = Tratamiento.builder()
                .atencionMedica(atencion)
                .indicaciones(request.getIndicaciones())
                .recomendaciones(request.getRecomendaciones())
                .build();

        tratamiento = tratamientoRepository.save(tratamiento);
        return mapper.toResponse(tratamiento);
    }

    @Transactional
    public AntecedenteClinicoResponseDTO registrarAntecedente(Long idPaciente, AntecedenteClinicoRequestDTO request,
                                                              String usuarioRegistro) {
        Paciente paciente = pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado."));

        HistoriaClinica historia = obtenerOCrear(paciente);

        AntecedenteClinico antecedente = AntecedenteClinico.builder()
                .historiaClinica(historia)
                .tipo(request.getTipo())
                .descripcion(request.getDescripcion())
                .usuarioRegistro(usuarioRegistro)
                .build();

        antecedente = antecedenteClinicoRepository.save(antecedente);
        return mapper.toResponse(antecedente);
    }

    @Transactional
    public AtencionMedicaResponseDTO cerrarAtencion(Long idAtencion, AtencionMedicaCierreRequestDTO request) {
        AtencionMedica atencion = obtenerAtencionValidada(idAtencion);

        List<Diagnostico> diagnosticos = diagnosticoRepository.findByAtencionMedica_IdAtencion(idAtencion);
        if (diagnosticos.isEmpty()) {
            throw new BusinessException("Debe registrar al menos un diagnóstico antes de cerrar la atención.");
        }

        atencion.setObservaciones(request.getObservaciones());
        atencion.setEstado("CERRADA");
        atencion.setFechaCierre(java.time.LocalDateTime.now());

        atencion = atencionMedicaRepository.save(atencion);

        List<Tratamiento> tratamientos = tratamientoRepository.findByAtencionMedica_IdAtencion(idAtencion);
        SignoVital signo = signoVitalRepository.findByAtencionMedica_IdAtencion(idAtencion).orElse(null);

        return mapper.toResponse(atencion, signo, diagnosticos, tratamientos);
    }

    @Transactional
    public byte[] generarYEnviarConstancia(Long idAtencion) {
        AtencionMedica atencion = atencionMedicaRepository.findById(idAtencion)
                .orElseThrow(() -> new ResourceNotFoundException("Atención médica no encontrada."));

        if (!"CERRADA".equals(atencion.getEstado())) {
            throw new BusinessException("Solo se puede generar la constancia de una atención cerrada.");
        }

        List<Diagnostico> diagnosticos = diagnosticoRepository.findByAtencionMedica_IdAtencion(idAtencion);
        List<Tratamiento> tratamientos = tratamientoRepository.findByAtencionMedica_IdAtencion(idAtencion);

        byte[] pdf = atencionMedicaPdfService.generarPdf(atencion, diagnosticos, tratamientos);

        emailService.enviarConstanciaAtencion(
                atencion.getCita().getPaciente().getPersona().getUsuario().getCorreo(),
                atencion.getCita().getPaciente().getPersona().getNombres(),
                pdf);

        return pdf;
    }

    @Transactional
    public DocumentoClinicoResponseDTO subirDocumento(Long idPaciente, Long idAtencion,
                                                      MultipartFile archivo, String tipoDocumento,
                                                      String usuarioCarga) {
        Paciente paciente = pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado."));

        HistoriaClinica historia = obtenerOCrear(paciente);

        AtencionMedica atencion = idAtencion != null ? obtenerAtencionValidada(idAtencion) : null;

        Map<String, String> resultado = documentoClinicoStorageService.subir(archivo, idPaciente);

        DocumentoClinico documento = DocumentoClinico.builder()
                .historiaClinica(historia)
                .atencionMedica(atencion)
                .nombreArchivo(archivo.getOriginalFilename())
                .urlArchivo(resultado.get("url"))
                .urlArchivoPublicId(resultado.get("publicId"))
                .tipoDocumento(tipoDocumento)
                .usuarioCarga(usuarioCarga)
                .build();

        documento = documentoClinicoRepository.save(documento);
        return mapper.toResponse(documento);
    }

    @Transactional
    public DocumentoClinicoResponseDTO actualizarDocumento(Long idDocumento, DocumentoClinicoRequestDTO request) {
        DocumentoClinico documento = documentoClinicoRepository.findById(idDocumento)
                .orElseThrow(() -> new ResourceNotFoundException("Documento no encontrado."));

        documento.setTipoDocumento(request.getTipoDocumento());
        documento = documentoClinicoRepository.save(documento);

        return mapper.toResponse(documento);
    }

    @Transactional
    public DocumentoClinicoResponseDTO reemplazarArchivoDocumento(Long idDocumento, MultipartFile archivo) {
        DocumentoClinico documento = documentoClinicoRepository.findById(idDocumento)
                .orElseThrow(() -> new ResourceNotFoundException("Documento no encontrado."));

        if (documento.getUrlArchivoPublicId() != null) {
            documentoClinicoStorageService.eliminar(documento.getUrlArchivoPublicId());
        }

        Map<String, String> resultado = documentoClinicoStorageService.subir(
                archivo, documento.getHistoriaClinica().getPaciente().getIdPaciente());

        documento.setNombreArchivo(archivo.getOriginalFilename());
        documento.setUrlArchivo(resultado.get("url"));
        documento.setUrlArchivoPublicId(resultado.get("publicId"));

        documento = documentoClinicoRepository.save(documento);
        return mapper.toResponse(documento);
    }

    @Transactional
    public void eliminarDocumento(Long idDocumento) {
        DocumentoClinico documento = documentoClinicoRepository.findById(idDocumento)
                .orElseThrow(() -> new ResourceNotFoundException("Documento no encontrado."));

        if (documento.getUrlArchivoPublicId() != null) {
            documentoClinicoStorageService.eliminar(documento.getUrlArchivoPublicId());
        }

        documentoClinicoRepository.delete(documento);
    }

    public DocumentoClinicoResponseDTO consultarDocumentoPorId(Long idDocumento) {
        DocumentoClinico documento = documentoClinicoRepository.findById(idDocumento)
                .orElseThrow(() -> new ResourceNotFoundException("Documento no encontrado."));
        return mapper.toResponse(documento);
    }

    public List<DocumentoClinicoResponseDTO> listarDocumentosPorPaciente(Long idPaciente) {
        HistoriaClinica historia = historiaClinicaRepository.findByPaciente_IdPaciente(idPaciente)
                .orElseThrow(() -> new ResourceNotFoundException("Este paciente no tiene historia clínica registrada."));

        return documentoClinicoRepository.findByHistoriaClinica_IdHistoria(historia.getIdHistoria()).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<DocumentoClinicoResponseDTO> listarDocumentosPorAtencion(Long idAtencion) {
        return documentoClinicoRepository.findByAtencionMedica_IdAtencion(idAtencion).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public HistoriaClinicaResponseDTO consultarPorPaciente(Long idPaciente) {
        HistoriaClinica historia = historiaClinicaRepository.findByPaciente_IdPaciente(idPaciente)
                .orElseThrow(() -> new ResourceNotFoundException("Este paciente no tiene historia clínica registrada."));

        List<AntecedenteClinicoResponseDTO> antecedentes = antecedenteClinicoRepository
                .findByHistoriaClinica_IdHistoria(historia.getIdHistoria()).stream()
                .map(mapper::toResponse).toList();

        List<AtencionMedicaResponseDTO> atenciones = atencionMedicaRepository
                .findByHistoriaClinica_IdHistoriaOrderByFechaAtencionDesc(historia.getIdHistoria()).stream()
                .map(atencion -> {
                    SignoVital signo = signoVitalRepository.findByAtencionMedica_IdAtencion(atencion.getIdAtencion()).orElse(null);
                    List<Diagnostico> diagnosticos = diagnosticoRepository.findByAtencionMedica_IdAtencion(atencion.getIdAtencion());
                    List<Tratamiento> tratamientos = tratamientoRepository.findByAtencionMedica_IdAtencion(atencion.getIdAtencion());
                    return mapper.toResponse(atencion, signo, diagnosticos, tratamientos);
                }).toList();

        List<DocumentoClinicoResponseDTO> documentos = documentoClinicoRepository
                .findByHistoriaClinica_IdHistoria(historia.getIdHistoria()).stream()
                .map(mapper::toResponse).toList();

        return mapper.toResponse(historia, antecedentes, atenciones, documentos);
    }

    private AtencionMedica obtenerAtencionValidada(Long idAtencion) {
        AtencionMedica atencion = atencionMedicaRepository.findById(idAtencion)
                .orElseThrow(() -> new ResourceNotFoundException("Atención médica no encontrada."));

        if ("CERRADA".equals(atencion.getEstado())) {
            throw new BusinessException("No se puede modificar una atención médica ya cerrada.");
        }

        return atencion;
    }
}