package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.response.AntecedentePacienteResponseDTO;
import com.Backend.MediConnect.clinica.persistance.entity.AntecedentePaciente;

import org.springframework.stereotype.Component;

@Component
public class AntecedentePacienteMapper {

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

                                .build();

        }

}