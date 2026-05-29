package com.Backend.MediConnect.clinica.web.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.Backend.MediConnect.clinica.domain.dto.*;
import com.Backend.MediConnect.clinica.persistance.entity.*;

public class EntityMapper {

    public static CitaResponseDTO toCitaResponse(Cita cita) {
        CitaResponseDTO dto = new CitaResponseDTO();
        dto.setIdCita(cita.getIdCita());
        dto.setFecha(cita.getFecha());
        dto.setHora(cita.getHora());
        dto.setEspecialidad(cita.getEspecialidad());
        dto.setTipo(cita.getTipo());
        dto.setPrioridad(cita.getPrioridad());
        dto.setDuracionEstimada(cita.getDuracionEstimada());
        dto.setEstado(cita.getEstado());
        if (cita.getMedico() != null) {
            dto.setNombreMedico(cita.getMedico().getPrimerNombre() + " " + cita.getMedico().getPrimerApellido());
            dto.setDniMedico(cita.getMedico().getDni());
        }
        if (cita.getPaciente() != null) {
            dto.setNombrePaciente(cita.getPaciente().getPrimerNombre() + " " + cita.getPaciente().getPrimerApellido());
            dto.setDniPaciente(cita.getPaciente().getDni());
        }
        if (cita.getSede() != null)
            dto.setNombreSede(cita.getSede().getNombreSede());
        return dto;
    }

    public static MedicoResponseDTO toMedicoResponse(Medico medico) {
        MedicoResponseDTO dto = new MedicoResponseDTO();
        dto.setIdMedico(medico.getIdMedico());
        dto.setPrimerNombre(medico.getPrimerNombre());
        dto.setSegundoNombre(medico.getSegundoNombre());
        dto.setPrimerApellido(medico.getPrimerApellido());
        dto.setSegundoApellido(medico.getSegundoApellido());
        dto.setDni(medico.getDni());
        dto.setEdad(medico.getEdad());
        dto.setDisponible(medico.getDisponible());

        if (medico.getEspecialidades() != null)
            dto.setEspecialidades(medico.getEspecialidades()
                    .stream()
                    .map(Especialidad::getNombreEspecialidad)
                    .collect(Collectors.toList()));

        if (medico.getSedes() != null)
            dto.setSedes(medico.getSedes()
                    .stream()
                    .map(Sede::getNombreSede)
                    .collect(Collectors.toList()));

        return dto;
    }

    public static PacienteResponseDTO toPacienteResponse(Paciente paciente) {
        PacienteResponseDTO dto = new PacienteResponseDTO();
        dto.setIdPaciente(paciente.getIdPaciente());
        dto.setPrimerNombre(paciente.getPrimerNombre());
        dto.setSegundoNombre(paciente.getSegundoNombre());
        dto.setPrimerApellido(paciente.getPrimerApellido());
        dto.setSegundoApellido(paciente.getSegundoApellido());
        dto.setDni(paciente.getDni());
        dto.setCorreo(paciente.getCorreo());
        dto.setTelefono(paciente.getTelefono());
        dto.setFechaNacimiento(paciente.getFechaNacimiento());
        dto.setUbigeo(paciente.getUbigeo());
        return dto;
    }

    public static HorarioResponseDTO toHorarioResponse(Horario horario) {
        HorarioResponseDTO dto = new HorarioResponseDTO();
        dto.setIdHorario(horario.getIdHorario());
        dto.setDiaSemana(horario.getDiaSemana());
        dto.setHoraInicio(horario.getHoraInicio());
        dto.setHoraFin(horario.getHoraFin());
        dto.setIntervaloMinutos(horario.getIntervaloMinutos());
        dto.setEstado(horario.getEstado());
        if (horario.getMedico() != null) {
            dto.setNombreMedico(horario.getMedico().getPrimerNombre() + " " + horario.getMedico().getPrimerApellido());
            dto.setDniMedico(horario.getMedico().getDni());
        }
        return dto;
    }

    public static RecetaResponseDTO toRecetaResponse(Receta receta) {
        RecetaResponseDTO dto = new RecetaResponseDTO();
        dto.setIdReceta(receta.getIdReceta());
        dto.setPrescripcion(receta.getPrescripcion());
        dto.setFecha(receta.getFecha());
        if (receta.getMedico() != null)
            dto.setNombreMedico(receta.getMedico().getPrimerNombre() + " " + receta.getMedico().getPrimerApellido());
        if (receta.getPaciente() != null)
            dto.setNombrePaciente(
                    receta.getPaciente().getPrimerNombre() + " " + receta.getPaciente().getPrimerApellido());
        if (receta.getConsulta() != null)
            dto.setIdConsulta(receta.getConsulta().getIdConsulta());
        return dto;
    }

    public static ReporteResponseDTO toReporteResponse(Reporte reporte) {
        ReporteResponseDTO dto = new ReporteResponseDTO();
        dto.setIdReporte(reporte.getIdReporte());
        dto.setFechaReporte(reporte.getFechaReporte());
        dto.setCitasAtendidas(reporte.getCitasAtendidas());
        dto.setCitasCanceladas(reporte.getCitasCanceladas());
        dto.setCitasReprogramadas(reporte.getCitasReprogramadas());
        dto.setCitasPendientes(reporte.getCitasPendientes());
        return dto;
    }

    public static AdminLocalResponseDTO toAdminLocalResponse(AdministadorLocal admin) {
        AdminLocalResponseDTO dto = new AdminLocalResponseDTO();
        dto.setIdAdminLocal(admin.getIdAdminLocal());
        dto.setPrimerNombre(admin.getPrimerNombre());
        dto.setSegundoNombre(admin.getSegundoNombre());
        dto.setPrimerApellido(admin.getPrimerApellido());
        dto.setSegundoApellido(admin.getSegundoApellido());
        dto.setDni(admin.getDni());
        if (admin.getSede() != null)
            dto.setNombreSede(admin.getSede().getNombreSede());
        return dto;
    }

    public static EspecialidadResponseDTO toEspecialidadResponse(Especialidad especialidad) {
        EspecialidadResponseDTO dto = new EspecialidadResponseDTO();
        dto.setIdEspecialidad(especialidad.getIdEspecialidad());
        dto.setNombreEspecialidad(especialidad.getNombreEspecialidad());
        return dto;
    }

    public static SedeResponseDTO toSedeResponse(Sede sede) {
        SedeResponseDTO dto = new SedeResponseDTO();
        dto.setIdSede(sede.getIdSede());
        dto.setNombreSede(sede.getNombreSede());
        dto.setUbicacion(sede.getUbicacion());
        return dto;
    }
}