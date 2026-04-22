package com.Backend.MediConnect.market.domain.service;

import com.Backend.MediConnect.market.domain.dto.consultarHorarioDTO;
import com.Backend.MediConnect.market.domain.dto.crearHorarioDTO;
import com.Backend.MediConnect.market.domain.repository.HorarioRepository;
import com.Backend.MediConnect.market.persistance.entity.Horario;
import com.Backend.MediConnect.market.persistance.entity.Medico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    public consultarHorarioDTO crearHorario(crearHorarioDTO request) {
        Horario horario = new Horario();
        // Simulamos la asignación del médico (en un caso real, buscarías el médico en su repositorio)
        Medico medico = new Medico();
        medico.setIdMedico(request.getIdMedico());
        horario.setMedico(medico);
        
        horario.setDiaSemana(request.getDiaSemana());
        horario.setHoraInicio(request.getHoraInicio());
        horario.setHoraFin(request.getHoraFin());
        horario.setIntervaloMinutos(request.getIntervaloMinutos());
        horario.setEstado("DISPONIBLE"); // Estado por defecto

        Horario horarioGuardado = horarioRepository.save(horario);
        return convertirADTO(horarioGuardado);
    }

    public consultarHorarioDTO cancelarHorario(Integer idHorario) {
        Horario horario = horarioRepository.findById(idHorario)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        horario.setEstado("CANCELADO");
        return convertirADTO(horarioRepository.save(horario));
    }

    public consultarHorarioDTO bloquearHorario(Integer idHorario) {
        Horario horario = horarioRepository.findById(idHorario)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        horario.setEstado("BLOQUEADO");
        return convertirADTO(horarioRepository.save(horario));
    }

    // Método auxiliar para no repetir código de mapeo
    private consultarHorarioDTO convertirADTO(Horario horario) {
        consultarHorarioDTO dto = new consultarHorarioDTO();
        dto.setIdHorario(horario.getIdHorario());
        dto.setDiaSemana(horario.getDiaSemana());
        dto.setHoraInicio(horario.getHoraInicio());
        dto.setHoraFin(horario.getHoraFin());
        dto.setIntervaloMinutos(horario.getIntervaloMinutos());
        dto.setEstado(horario.getEstado());
        return dto;
    }
}