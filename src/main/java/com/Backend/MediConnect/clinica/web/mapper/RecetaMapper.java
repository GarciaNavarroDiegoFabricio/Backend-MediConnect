package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.response.*;
import com.Backend.MediConnect.clinica.persistance.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecetaMapper {

        public DetalleRecetaResponseDTO toResponse(
                        DetalleReceta detalle) {

                return DetalleRecetaResponseDTO.builder()
                                .idDetalleReceta(detalle.getIdDetalleReceta())
                                .medicamento(detalle.getMedicamento())
                                .dosis(detalle.getDosis())
                                .frecuencia(detalle.getFrecuencia())
                                .duracion(detalle.getDuracion())
                                .indicaciones(detalle.getIndicaciones())
                                .build();

        }

        public RecetaResponseDTO toResponse(
                        Receta receta,
                        List<DetalleReceta> detalles) {

                return RecetaResponseDTO.builder()
                                .idReceta(receta.getIdReceta())
                                .idConsulta(receta.getConsulta().getIdConsulta())
                                .nombrePaciente(
                                                construirNombre(
                                                                receta.getPaciente()
                                                                                .getPersona()
                                                                                .getNombres(),

                                                                receta.getPaciente()
                                                                                .getPersona()
                                                                                .getApellidoPaterno(),

                                                                receta.getPaciente()
                                                                                .getPersona()
                                                                                .getApellidoMaterno()))
                                .nombreMedico(
                                                construirNombre(
                                                                receta.getMedico()
                                                                                .getPersona()
                                                                                .getNombres(),

                                                                receta.getMedico()
                                                                                .getPersona()
                                                                                .getApellidoPaterno(),

                                                                receta.getMedico()
                                                                                .getPersona()
                                                                                .getApellidoMaterno()))
                                .prescripcion(receta.getPrescripcion())
                                .fecha(receta.getFecha())
                                .detalles(
                                                detalles.stream()
                                                                .map(this::toResponse)
                                                                .toList())
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

        private DetalleRecetaResponseDTO detalleToResponse(
                        DetalleReceta detalle) {

                return DetalleRecetaResponseDTO.builder()

                                .idDetalleReceta(
                                                detalle.getIdDetalleReceta())

                                .medicamento(
                                                detalle.getMedicamento())

                                .dosis(
                                                detalle.getDosis())

                                .frecuencia(
                                                detalle.getFrecuencia())

                                .duracion(
                                                detalle.getDuracion())

                                .indicaciones(
                                                detalle.getIndicaciones())

                                .build();
        }

}