package com.Backend.MediConnect.clinica.domain.interfaces;

import java.util.List;

import com.Backend.MediConnect.clinica.domain.dto.EspecialidadRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.EspecialidadResponseDTO;

public interface IEspecialidadService {

    EspecialidadResponseDTO registrarEspecialidad(EspecialidadRequestDTO dto);

    EspecialidadResponseDTO actualizarEspecialidad(Integer id, EspecialidadRequestDTO dto);

    void inactivarEspecialidad(Integer id);

    void activarEspecialidad(Integer id);

    List<EspecialidadResponseDTO> listarEspecialidades();

    EspecialidadResponseDTO obtenerEspecialidad(Integer id);
}