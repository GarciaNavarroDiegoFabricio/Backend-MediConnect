package com.Backend.MediConnect.clinica.domain.interfaces;

import com.Backend.MediConnect.clinica.domain.dto.response.ReniecResponseDTO;

public interface IReniecService {
    ReniecResponseDTO consultarDni(String dni);
}