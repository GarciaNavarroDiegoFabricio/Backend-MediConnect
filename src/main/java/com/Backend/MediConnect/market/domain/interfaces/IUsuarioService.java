package com.Backend.MediConnect.market.domain.interfaces;

import com.Backend.MediConnect.market.domain.dto.*;

public interface IUsuarioService {
    AuthResponse login(AuthRequest request);
    AuthResponse registrarPaciente(RegistroPacienteDTO dto);
    AuthResponse registrarAdminLocal(RegistroAdminLocalDTO dto);
    AuthResponse registrarAdminTotal(RegistroAdminTotalDTO dto);
    AuthResponse registrarMedico(RegistroMedicoDTO dto);
}