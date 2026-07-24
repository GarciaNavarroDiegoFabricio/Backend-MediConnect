package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.response.*;
import com.Backend.MediConnect.clinica.persistance.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HistoriaClinicaMapper {

        public AntecedentePacienteResponseDTO toResponse(
                        AntecedentePaciente antecedente) {

                return AntecedentePacienteResponseDTO.builder()

                                .idAntecedente(
                                                antecedente.getIdAntecedente())

                                .antecedentesPersonales(
                                                antecedente.getAntecedentesPersonales())

                                .antecedentesFamiliares(
                                                antecedente.getAntecedentesFamiliares())

                                .alergias(
                                                antecedente.getAlergias())

                                .condicionesRelevantes(
                                                antecedente.getCondicionesRelevantes())

                                .fechaActualizacion(
                                                antecedente.getFechaActualizacion())

                                .usuarioModificacion(
                                                antecedente.getUsuarioModificacion())

                                .build();
        }

        public SignoVitalResponseDTO toResponse(SignoVital signo) {

                return SignoVitalResponseDTO.builder()

                                .idSigno(signo.getIdSigno())

                                .presionArterial(signo.getPresionArterial())

                                .frecuenciaCardiaca(signo.getFrecuenciaCardiaca())

                                .temperatura(
                                                signo.getTemperatura() != null
                                                                ? signo.getTemperatura().doubleValue()
                                                                : null)

                                .peso(
                                                signo.getPeso() != null
                                                                ? signo.getPeso().doubleValue()
                                                                : null)

                                .talla(
                                                signo.getTalla() != null
                                                                ? signo.getTalla().doubleValue()
                                                                : null)

                                .build();
        }

        public DiagnosticoMedicoResponseDTO toResponse(DiagnosticoMedico diagnostico) {

                return DiagnosticoMedicoResponseDTO.builder()
                                .idDiagnostico(diagnostico.getIdDiagnostico())
                                .descripcionClinica(diagnostico.getDescripcionClinica())
                                .categoriaDiagnostica(diagnostico.getCategoriaDiagnostica())
                                .fechaRegistro(diagnostico.getFechaRegistro())
                                .build();
        }

        public DetalleAtencionResponseDTO toResponse(DetalleAtencionMedica detalle) {

                return DetalleAtencionResponseDTO.builder()
                                .idDetalle(detalle.getIdDetalle())
                                .tratamiento(detalle.getTratamiento())
                                .indicacionesMedicas(detalle.getIndicacionesMedicas())
                                .observaciones(detalle.getObservaciones())
                                .recomendaciones(detalle.getRecomendaciones())
                                .fechaRegistro(detalle.getFechaRegistro())
                                .build();
        }

        public DocumentoClinicoResponseDTO toResponse(DocumentoClinico documento) {

                return DocumentoClinicoResponseDTO.builder()

                                .idDocumento(documento.getIdDocumento())

                                .idConsulta(
                                                documento.getConsulta() != null
                                                                ? documento.getConsulta().getIdConsulta()
                                                                : null)

                                .nombreArchivo(documento.getNombreArchivo())

                                .tipoDocumento(documento.getTipoDocumento())

                                .rutaArchivo(documento.getRutaArchivo())

                                .fechaSubida(documento.getFechaSubida())

                                .build();
        }

        public ConsultaResponseDTO toResponse(
                        Consulta consulta,
                        SignoVital signo,
                        List<DiagnosticoMedico> diagnosticos,
                        DetalleAtencionMedica detalle) {

                return ConsultaResponseDTO.builder()
                                .idConsulta(consulta.getIdConsulta())
                                .idPaciente(consulta.getPaciente().getIdPaciente())

                                .nombrePaciente(
                                                construirNombre(
                                                                consulta.getPaciente()
                                                                                .getPersona()
                                                                                .getNombres(),

                                                                consulta.getPaciente()
                                                                                .getPersona()
                                                                                .getApellidoPaterno(),

                                                                consulta.getPaciente()
                                                                                .getPersona()
                                                                                .getApellidoMaterno()))

                                .nombreMedico(
                                                construirNombre(
                                                                consulta.getMedico()
                                                                                .getPersona()
                                                                                .getNombres(),

                                                                consulta.getMedico()
                                                                                .getPersona()
                                                                                .getApellidoPaterno(),

                                                                consulta.getMedico()
                                                                                .getPersona()
                                                                                .getApellidoMaterno()))

                                .estado(consulta.getEstado())
                                .horaInicio(consulta.getHoraInicio())
                                .horaFin(consulta.getHoraFin())

                                .signoVital(
                                                signo != null
                                                                ? toResponse(signo)
                                                                : null)

                                .diagnosticos(
                                                diagnosticos != null
                                                                ? diagnosticos.stream()
                                                                                .map(this::toResponse)
                                                                                .toList()
                                                                : List.of())

                                .detalleAtencion(
                                                detalle != null
                                                                ? toResponse(detalle)
                                                                : null)

                                .build();
        }

        public HistoriaClinicaResponseDTO toResponse(
                        HistoriaClinica historia,
                        List<AntecedentePacienteResponseDTO> antecedentes,
                        List<ConsultaResponseDTO> consultas,
                        List<DocumentoClinicoResponseDTO> documentos) {

                return HistoriaClinicaResponseDTO.builder()
                                .idHistoria(historia.getIdHistoria())

                                .idPaciente(
                                                historia.getPaciente()
                                                                .getIdPaciente())

                                .nombrePaciente(
                                                construirNombre(
                                                                historia.getPaciente()
                                                                                .getPersona()
                                                                                .getNombres(),

                                                                historia.getPaciente()
                                                                                .getPersona()
                                                                                .getApellidoPaterno(),

                                                                historia.getPaciente()
                                                                                .getPersona()
                                                                                .getApellidoMaterno()))

                                .fechaCreacion(historia.getFechaCreacion())

                                .antecedentes(antecedentes)

                                .consultas(consultas)

                                .documentos(documentos)

                                .build();
        }

        private String construirNombre(
                        String nombres,
                        String apellidoPaterno,
                        String apellidoMaterno) {

                return String.join(" ",
                                nombres != null ? nombres : "",
                                apellidoPaterno != null ? apellidoPaterno : "",
                                apellidoMaterno != null ? apellidoMaterno : "")
                                .trim();
        }

}