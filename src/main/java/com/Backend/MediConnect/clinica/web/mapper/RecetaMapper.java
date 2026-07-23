package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.response.DetalleRecetaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.RecetaResponseDTO;
import com.Backend.MediConnect.clinica.persistance.entity.DetalleReceta;
import com.Backend.MediConnect.clinica.persistance.entity.Receta;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecetaMapper {

    public DetalleRecetaResponseDTO toResponse(DetalleReceta detalle) {
        return DetalleRecetaResponseDTO.builder()
                .idDetalleReceta(detalle.getIdDetalleReceta())
                .medicamento(detalle.getMedicamento())
                .dosis(detalle.getDosis())
                .frecuencia(detalle.getFrecuencia())
                .duracion(detalle.getDuracion())
                .indicaciones(detalle.getIndicaciones())
                .build();
    }

    public RecetaResponseDTO toResponse(Receta receta, List<DetalleReceta> detalles) {
        return RecetaResponseDTO.builder()
                .idReceta(receta.getIdReceta())
                .idAtencion(receta.getAtencionMedica().getIdAtencion())
                .codigoReceta(receta.getCodigoReceta())
                .nombrePaciente(construirNombre(
                        receta.getAtencionMedica().getCita().getPaciente().getPersona().getNombres(),
                        receta.getAtencionMedica().getCita().getPaciente().getPersona().getApellidoPaterno(),
                        receta.getAtencionMedica().getCita().getPaciente().getPersona().getApellidoMaterno()))
                .nombreMedico(construirNombre(
                        receta.getAtencionMedica().getCita().getMedico().getPersona().getNombres(),
                        receta.getAtencionMedica().getCita().getMedico().getPersona().getApellidoPaterno(),
                        receta.getAtencionMedica().getCita().getMedico().getPersona().getApellidoMaterno()))
                .especialidad(receta.getAtencionMedica().getCita().getMedico().getEspecialidad().getNombre())
                .observaciones(receta.getObservaciones())
                .fechaEmision(receta.getFechaEmision())
                .detalles(detalles.stream().map(this::toResponse).toList())
                .build();
    }

    private String construirNombre(String nombres, String apellidoPaterno, String apellidoMaterno) {
        return String.join(" ",
                nombres != null ? nombres : "",
                apellidoPaterno != null ? apellidoPaterno : "",
                apellidoMaterno != null ? apellidoMaterno : "").trim();
    }
}