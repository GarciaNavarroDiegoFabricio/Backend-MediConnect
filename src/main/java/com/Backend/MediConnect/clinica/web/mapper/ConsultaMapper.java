package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.response.*;
import com.Backend.MediConnect.clinica.persistance.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultaMapper {

    public ConsultaResponseDTO toResponse(
            Consulta consulta,
            SignoVital signo,
            List<DiagnosticoMedico> diagnosticos,
            DetalleAtencionMedica detalle) {

        return ConsultaResponseDTO.builder()

                .idConsulta(consulta.getIdConsulta())

                .idPaciente(
                        consulta.getPaciente()
                                .getIdPaciente())

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
                                ? convertirSigno(signo)
                                : null)

                .diagnosticos(
                        diagnosticos != null
                                ? diagnosticos.stream()
                                        .map(this::convertirDiagnostico)
                                        .toList()
                                : List.of())

                .detalleAtencion(
                        detalle != null
                                ? convertirDetalle(detalle)
                                : null)

                .build();

    }

    private SignoVitalResponseDTO convertirSigno(SignoVital signo) {

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

    private DiagnosticoMedicoResponseDTO convertirDiagnostico(
            DiagnosticoMedico diagnostico) {

        return DiagnosticoMedicoResponseDTO.builder()

                .idDiagnostico(diagnostico.getIdDiagnostico())

                .descripcionClinica(
                        diagnostico.getDescripcionClinica())

                .categoriaDiagnostica(
                        diagnostico.getCategoriaDiagnostica())

                .build();
    }

    private DetalleAtencionResponseDTO convertirDetalle(
            DetalleAtencionMedica detalle) {

        return DetalleAtencionResponseDTO.builder()

                .idDetalle(detalle.getIdDetalle())

                .tratamiento(detalle.getTratamiento())

                .indicacionesMedicas(
                        detalle.getIndicacionesMedicas())

                .observaciones(
                        detalle.getObservaciones())

                .recomendaciones(
                        detalle.getRecomendaciones())

                .build();

    }

    private String construirNombre(
            String nombres,
            String paterno,
            String materno) {

        return String.join(" ",
                nombres != null ? nombres : "",
                paterno != null ? paterno : "",
                materno != null ? materno : "")
                .trim();

    }

}