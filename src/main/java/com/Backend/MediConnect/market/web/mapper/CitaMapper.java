package com.Backend.MediConnect.market.web.mapper;

import com.Backend.MediConnect.market.domain.dto.cancelarCitaDTO;
import com.Backend.MediConnect.market.domain.dto.consultarCitaDTO;
import com.Backend.MediConnect.market.domain.dto.crearCitaDTO;
import com.Backend.MediConnect.market.persistance.entity.Cita;
import com.Backend.MediConnect.market.persistance.entity.Medico;
import com.Backend.MediConnect.market.persistance.entity.Paciente;
import com.Backend.MediConnect.market.persistance.entity.Sede;
import org.springframework.stereotype.Component;

@Component
public class CitaMapper {

    public Cita crearToEntity(crearCitaDTO dto, Medico medico, Paciente paciente, Sede sede){
        Cita cita = new Cita();
        cita.setFecha(dto.getFecha());
        cita.setHora(dto.getHora());
        cita.setEspecialidad(dto.getEspecialidad());
        cita.setDuracionEstimada(dto.getDuracionEstimada());
        cita.setEstado(dto.getEstado());
        cita.setTipo(dto.getTipo());
        cita.setPrioridad(dto.getPrioridad());

        cita.setMedico(medico);
        cita.setPaciente(paciente);
        cita.setSede(sede);

        return cita;
    }


    public consultarCitaDTO consultaToDTO(Cita cita) {

        consultarCitaDTO dto = new consultarCitaDTO();

        dto.setFecha(cita.getFecha());
        dto.setHora(cita.getHora());

        dto.setEspecialidad(cita.getEspecialidad());
        dto.setDuracionEstimada(cita.getDuracionEstimada());
        dto.setEstado(cita.getEstado());
        dto.setTipo(cita.getTipo());
        dto.setPrioridad(cita.getPrioridad());

        dto.setNombreMedico(
                cita.getMedico().getPrimerNombre() + " " + cita.getMedico().getPrimerApellido()
        );

        dto.setNombrePaciente(
                cita.getPaciente().getPrimerNombre() + " " + cita.getPaciente().getSegundoApellido()
        );

        dto.setNombreSede(cita.getSede().getNombreSede());

        return dto;
    }

    public cancelarCitaDTO cancelarToDto(Cita cita){
        cancelarCitaDTO dto = new cancelarCitaDTO();
        dto.setIdCita(cita.getIdCita());
        dto.setEstado(cita.getEstado());
        dto.setFecha(cita.getFecha().toString());
        dto.setHora(cita.getHora().toString());

        return dto;
    }


}
