package com.Backend.MediConnect.clinica.domain.interfaces;

import java.util.List;

import com.Backend.MediConnect.clinica.domain.dto.HorarioDTO;
import com.Backend.MediConnect.clinica.domain.dto.HorarioResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.MedicoResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.ReprogramarHorarioDTO;

public interface IAdminLocalService {
    HorarioResponseDTO crearHorario(HorarioDTO dto);

    HorarioResponseDTO reprogramarHorario(Integer idHorario, ReprogramarHorarioDTO dto);

    void cancelarHorario(Integer idHorario);

    void bloquearHorario(Integer idHorario);

    List<MedicoResponseDTO> gestionarMedico(Integer idSede);

    // NUEVO REQUISITO: Cambiar estado del médico
    void cambiarEstadoMedico(Integer idMedico, String nuevoEstado);
}