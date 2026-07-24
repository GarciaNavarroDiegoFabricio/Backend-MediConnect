package com.Backend.MediConnect.clinica.web.mapper;

import org.springframework.stereotype.Component;

import com.Backend.MediConnect.clinica.domain.dto.response.PacienteResponseDTO;
import com.Backend.MediConnect.clinica.persistance.entity.Paciente;

@Component
public class PacienteMapper {

    public PacienteResponseDTO toResponse(
            Paciente paciente) {

        return PacienteResponseDTO.builder()

                .idPaciente(
                        paciente.getIdPaciente())

                .codigoHistoriaClinica(
                        paciente.getCodigoHistoriaClinica())

                .nombres(
                        paciente.getPersona().getNombres())

                .apellidoPaterno(
                        paciente.getPersona().getApellidoPaterno())

                .apellidoMaterno(
                        paciente.getPersona().getApellidoMaterno())

                .dni(
                        paciente.getPersona().getDni())

                .correo(
                        paciente.getPersona()
                                .getUsuario()
                                .getCorreo())

                .build();

    }

}
