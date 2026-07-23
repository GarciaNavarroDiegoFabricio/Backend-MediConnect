package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.response.*;
import com.Backend.MediConnect.clinica.persistance.entity.*;
import org.springframework.stereotype.Component;

@Component
public class HistoriaClinicaMapper {

    public AntecedenteClinicoResponseDTO toResponse(AntecedenteClinico antecedente) {
        return AntecedenteClinicoResponseDTO.builder()
                .idAntecedente(antecedente.getIdAntecedente())
                .tipo(antecedente.getTipo())
                .descripcion(antecedente.getDescripcion())
                .fechaRegistro(antecedente.getFechaRegistro())
                .usuarioRegistro(antecedente.getUsuarioRegistro())
                .build();
    }

    public SignoVitalResponseDTO toResponse(SignoVital signo) {
        return SignoVitalResponseDTO.builder()
                .idSigno(signo.getIdSigno())
                .presionArterial(signo.getPresionArterial())
                .frecuenciaCardiaca(signo.getFrecuenciaCardiaca())
                .frecuenciaRespiratoria(signo.getFrecuenciaRespiratoria())
                .temperatura(signo.getTemperatura())
                .saturacionOxigeno(signo.getSaturacionOxigeno())
                .peso(signo.getPeso())
                .talla(signo.getTalla())
                .build();
    }

    public DiagnosticoResponseDTO toResponse(Diagnostico diagnostico) {
        return DiagnosticoResponseDTO.builder()
                .idDiagnostico(diagnostico.getIdDiagnostico())
                .codigoCie10(diagnostico.getCodigoCie10())
                .descripcion(diagnostico.getDescripcion())
                .tipo(diagnostico.getTipo())
                .build();
    }

    public TratamientoResponseDTO toResponse(Tratamiento tratamiento) {
        return TratamientoResponseDTO.builder()
                .idTratamiento(tratamiento.getIdTratamiento())
                .indicaciones(tratamiento.getIndicaciones())
                .recomendaciones(tratamiento.getRecomendaciones())
                .build();
    }

    public DocumentoClinicoResponseDTO toResponse(DocumentoClinico documento) {
        return DocumentoClinicoResponseDTO.builder()
                .idDocumento(documento.getIdDocumento())
                .idHistoria(documento.getHistoriaClinica().getIdHistoria())
                .idAtencion(documento.getAtencionMedica() != null ? documento.getAtencionMedica().getIdAtencion() : null)
                .nombreArchivo(documento.getNombreArchivo())
                .urlArchivo(documento.getUrlArchivo())
                .tipoDocumento(documento.getTipoDocumento())
                .fechaCarga(documento.getFechaCarga())
                .usuarioCarga(documento.getUsuarioCarga())
                .build();
    }

    public AtencionMedicaResponseDTO toResponse(AtencionMedica atencion, SignoVital signo,
                                                java.util.List<Diagnostico> diagnosticos,
                                                java.util.List<Tratamiento> tratamientos) {
        return AtencionMedicaResponseDTO.builder()
                .idAtencion(atencion.getIdAtencion())
                .idCita(atencion.getCita().getIdCita())
                .nombrePaciente(construirNombre(
                        atencion.getCita().getPaciente().getPersona().getNombres(),
                        atencion.getCita().getPaciente().getPersona().getApellidoPaterno(),
                        atencion.getCita().getPaciente().getPersona().getApellidoMaterno()))
                .nombreMedico(construirNombre(
                        atencion.getCita().getMedico().getPersona().getNombres(),
                        atencion.getCita().getMedico().getPersona().getApellidoPaterno(),
                        atencion.getCita().getMedico().getPersona().getApellidoMaterno()))
                .motivoConsulta(atencion.getMotivoConsulta())
                .observaciones(atencion.getObservaciones())
                .estado(atencion.getEstado())
                .fechaAtencion(atencion.getFechaAtencion())
                .fechaCierre(atencion.getFechaCierre())
                .signoVital(signo != null ? toResponse(signo) : null)
                .diagnosticos(diagnosticos != null ? diagnosticos.stream().map(this::toResponse).toList() : java.util.List.of())
                .tratamientos(tratamientos != null ? tratamientos.stream().map(this::toResponse).toList() : java.util.List.of())
                .build();
    }

    public HistoriaClinicaResponseDTO toResponse(HistoriaClinica historia,
                                                 java.util.List<AntecedenteClinicoResponseDTO> antecedentes,
                                                 java.util.List<AtencionMedicaResponseDTO> atenciones,
                                                 java.util.List<DocumentoClinicoResponseDTO> documentos) {
        return HistoriaClinicaResponseDTO.builder()
                .idHistoria(historia.getIdHistoria())
                .idPaciente(historia.getPaciente().getIdPaciente())
                .nombrePaciente(construirNombre(
                        historia.getPaciente().getPersona().getNombres(),
                        historia.getPaciente().getPersona().getApellidoPaterno(),
                        historia.getPaciente().getPersona().getApellidoMaterno()))
                .fechaCreacion(historia.getFechaCreacion())
                .antecedentes(antecedentes)
                .atenciones(atenciones)
                .documentos(documentos)
                .build();
    }

    private String construirNombre(String nombres, String apellidoPaterno, String apellidoMaterno) {
        return String.join(" ",
                nombres != null ? nombres : "",
                apellidoPaterno != null ? apellidoPaterno : "",
                apellidoMaterno != null ? apellidoMaterno : "").trim();
    }
}