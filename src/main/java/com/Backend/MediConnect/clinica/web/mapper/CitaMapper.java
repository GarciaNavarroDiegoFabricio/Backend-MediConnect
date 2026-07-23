package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.response.CitaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.HistorialCitaResponseDTO;
import com.Backend.MediConnect.clinica.persistance.entity.Cita;
import com.Backend.MediConnect.clinica.persistance.entity.HistorialCita;
import org.springframework.stereotype.Component;

@Component
public class CitaMapper {

    public CitaResponseDTO toResponse(Cita cita) {
        return CitaResponseDTO.builder()
                .idCita(cita.getIdCita())
                .idPaciente(cita.getPaciente().getIdPaciente())
                .nombrePaciente(construirNombreCompleto(
                        cita.getPaciente().getPersona().getNombres(),
                        cita.getPaciente().getPersona().getApellidoPaterno(),
                        cita.getPaciente().getPersona().getApellidoMaterno()))
                .idMedico(cita.getMedico().getIdMedico())
                .nombreMedico(construirNombreCompleto(
                        cita.getMedico().getPersona().getNombres(),
                        cita.getMedico().getPersona().getApellidoPaterno(),
                        cita.getMedico().getPersona().getApellidoMaterno()))
                .nombreEspecialidad(cita.getMedico().getEspecialidad().getNombre())
                .fechaCita(cita.getFechaCita())
                .horaInicio(cita.getHoraInicio())
                .horaFin(cita.getHoraFin())
                .modalidad(cita.getModalidad())
                .enlaceVideollamada(cita.getEnlaceVideollamada())
                .motivoConsulta(cita.getMotivoConsulta())
                .estado(cita.getEstado())
                .fechaCreacion(cita.getFechaCreacion())
                .fechaModificacion(cita.getFechaModificacion())
                .build();
    }

    public HistorialCitaResponseDTO toHistorialResponse(HistorialCita historial) {
        return HistorialCitaResponseDTO.builder()
                .idHistorial(historial.getIdHistorial())
                .estadoAnterior(historial.getEstadoAnterior())
                .estadoNuevo(historial.getEstadoNuevo())
                .motivoCambio(historial.getMotivoCambio())
                .fechaCambio(historial.getFechaCambio())
                .usuarioCambio(historial.getUsuarioCambio())
                .build();
    }

    private String construirNombreCompleto(String nombres, String apellidoPaterno, String apellidoMaterno) {
        return String.join(" ",
                nombres != null ? nombres : "",
                apellidoPaterno != null ? apellidoPaterno : "",
                apellidoMaterno != null ? apellidoMaterno : "").trim();
    }
}