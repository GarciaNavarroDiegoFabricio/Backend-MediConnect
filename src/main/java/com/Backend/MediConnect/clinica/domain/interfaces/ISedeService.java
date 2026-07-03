package com.Backend.MediConnect.clinica.domain.interfaces;

import java.util.List;

import com.Backend.MediConnect.clinica.domain.dto.SedeRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.SedeResponseDTO;

public interface ISedeService {

    SedeResponseDTO registrarSede(SedeRequestDTO dto);

    SedeResponseDTO actualizarSede(Integer id, SedeRequestDTO dto);

    void inactivarSede(Integer id);

    void activarSede(Integer id);

    List<SedeResponseDTO> listarSedes();

    SedeResponseDTO obtenerSede(Integer id);
}