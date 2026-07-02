package com.Backend.MediConnect.clinica.domain.interfaces;

import java.util.List;

import com.Backend.MediConnect.clinica.domain.dto.BloquearHorarioDTO;
import com.Backend.MediConnect.clinica.domain.dto.EditarHorarioDTO;
import com.Backend.MediConnect.clinica.domain.dto.HorarioDTO;
import com.Backend.MediConnect.clinica.domain.dto.HorarioResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.MedicoResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.ReprogramarHorarioDTO; // 👈 NUEVO DTO

public interface IAdminLocalService {
    HorarioResponseDTO crearHorario(HorarioDTO dto);

    HorarioResponseDTO reprogramarHorario(Integer idHorario, ReprogramarHorarioDTO dto);

    void cancelarHorario(Integer idHorario);

    //PARA EL RF4: Ahora recibe el motivo del bloqueo
    void bloquearHorario(Integer idHorario, BloquearHorarioDTO dto);

    List<MedicoResponseDTO> gestionarMedico(Integer idSede);

    void cambiarEstadoMedico(Integer idMedico, String nuevoEstado);

    HorarioResponseDTO actualizarHorario(Integer idHorario, EditarHorarioDTO dto);
    
    void inactivarHorario(Integer idHorario);

    // AUTOMATIZACIÓN AGREGADA PARA EL RF2
    void verificarYActualizarDisponibilidadMedico(Integer idMedico);
}