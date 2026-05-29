package com.Backend.MediConnect.clinica.domain.interfaces;

import java.util.List;

import com.Backend.MediConnect.clinica.domain.dto.ActualizarContactoPacienteDTO;
import com.Backend.MediConnect.clinica.domain.dto.CitaDTO;
import com.Backend.MediConnect.clinica.domain.dto.CitaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.PacienteResponseDTO;

public interface IPacienteService {
    CitaResponseDTO generarCita(String dniPaciente, CitaDTO dto);
    List<CitaResponseDTO> consultarCitas(String dniPaciente);
    void cancelarCita(String dniPaciente, Integer idCita);
    PacienteResponseDTO obtenerPerfil(String dniPaciente);
    void actualizarContacto(String dniPaciente, ActualizarContactoPacienteDTO dto);
}