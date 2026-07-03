package com.Backend.MediConnect.clinica.domain.interfaces;

import com.Backend.MediConnect.clinica.domain.dto.*;

import java.util.List;

public interface IUsuarioService {
    AuthResponse login(AuthRequest request);

    AuthResponse registrarPaciente(RegistroPacienteDTO dto);

    AuthResponse registrarAdminLocal(RegistroAdminLocalDTO dto);

    AuthResponse registrarAdminTotal(RegistroAdminTotalDTO dto);

    AuthResponse registrarMedico(RegistroMedicoDTO dto, String dniRegistrador, String rolRegistrador);

    UsuarioPerfilDTO obtenerPerfil(String dni, String rol);

    List<UsuarioPerfilDTO> listarMedicos();
    List<UsuarioPerfilDTO> listarPacientes();
}