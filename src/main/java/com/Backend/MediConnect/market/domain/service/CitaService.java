package com.Backend.MediConnect.market.domain.service;

import com.Backend.MediConnect.market.domain.dto.cancelarCitaDTO;
import com.Backend.MediConnect.market.domain.dto.consultarCitaDTO;
import com.Backend.MediConnect.market.domain.dto.crearCitaDTO;
import com.Backend.MediConnect.market.domain.repository.*;
import com.Backend.MediConnect.market.persistance.entity.*;
import com.Backend.MediConnect.market.web.mapper.CitaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CitaService {
    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private SedeRepository sedeRepository;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private CitaMapper citaMapper;



    //Metodo con el que el paciente crea una nueva cita
    public consultarCitaDTO crearCita(crearCitaDTO request){
        Medico medico = medicoRepository.findById(request.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Medico no encontrado"));

        Paciente paciente = pacienteRepository.findById(request.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Sede sede = sedeRepository.findById(request.getIdSede())
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

        Cita cita = citaMapper.crearToEntity(request, medico, paciente, sede);

        Cita citaGuardada = citaRepository.save(cita);

        return citaMapper.consultaToDTO(citaGuardada);
    }

    //Metodo con el que el paciente puede consultar sus citas
    public List<consultarCitaDTO> obtenerCitasPorPaciente(Integer idPaciente) {
        return citaRepository.findByPaciente_IdPaciente(idPaciente)
                .stream()
                .map(citaMapper::consultaToDTO)
                .toList();
    }

    //Metodo con el que el paciente puede cancelar su cita.
    // Cambiando el estado de su cita a cancelado.
    public cancelarCitaDTO cancelarCita(Integer idCita, Integer idPaciente){
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        if(!cita.getPaciente().getIdPaciente().equals(idPaciente)){
            throw new RuntimeException("No puedes cancelar esta cita.");
        }else{
            cita.setEstado("CANCELADO");

            Cita citaActualizada = citaRepository.save(cita);

            // Se crea una nueva notificacion y guarda en la bd
            Notificacion noti = new Notificacion();
            noti.setCita(citaActualizada);
            noti.setPaciente(citaActualizada.getPaciente());
            noti.setMensaje("Tu cita ha sido cancelada");
            noti.setFecha(LocalDateTime.now());
            noti.setVisto(Boolean.FALSE);

            notificacionRepository.save(noti);

            return citaMapper.cancelarToDto(citaActualizada);
        }

    }



}
