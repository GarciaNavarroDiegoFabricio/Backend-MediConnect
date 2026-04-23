package com.Backend.MediConnect.market.domain.interfaces;

import com.Backend.MediConnect.market.domain.dto.HorarioDTO;
import com.Backend.MediConnect.market.domain.dto.HorarioResponseDTO;
import com.Backend.MediConnect.market.domain.dto.MedicoResponseDTO;
import com.Backend.MediConnect.market.domain.dto.ReprogramarHorarioDTO;
import java.util.List;

public interface IAdminLocalService {
    HorarioResponseDTO crearHorario(HorarioDTO dto);
    HorarioResponseDTO reprogramarHorario(Integer idHorario, ReprogramarHorarioDTO dto);
    void cancelarHorario(Integer idHorario);
    void bloquearHorario(Integer idHorario);
    List<MedicoResponseDTO> gestionarMedico(Integer idSede);
}