package com.Backend.MediConnect.clinica.domain.interfaces;

import com.Backend.MediConnect.clinica.domain.dto.PacienteResponseDTO;

public interface IPacienteConsultaService {
    PacienteResponseDTO buscarPorDni(String dni);
    void finalizarConsulta(Integer idConsulta); // Firma añadida
}
