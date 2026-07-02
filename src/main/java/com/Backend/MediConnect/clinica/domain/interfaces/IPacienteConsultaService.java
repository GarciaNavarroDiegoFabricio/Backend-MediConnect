package com.Backend.MediConnect.clinica.domain.interfaces;

import com.Backend.MediConnect.clinica.domain.dto.PacienteResponseDTO;

public interface IPacienteConsultaService {
    PacienteResponseDTO buscarPorDni(String dni);
}
