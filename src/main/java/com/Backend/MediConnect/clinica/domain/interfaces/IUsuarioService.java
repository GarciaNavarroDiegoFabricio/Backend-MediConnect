package com.Backend.MediConnect.clinica.domain.interfaces;

import com.Backend.MediConnect.clinica.domain.dto.request.UsuarioRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.UsuarioUpdateRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.UsuarioResponseDTO;

import java.util.List;

public interface IUsuarioService {
    UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request, String usuarioCreacion);
    UsuarioResponseDTO actualizarUsuario(Long idUsuario, UsuarioUpdateRequestDTO request, String usuarioModificacion);
    UsuarioResponseDTO consultarPorId(Long idUsuario);
    List<UsuarioResponseDTO> listarUsuarios();
    void bloquearUsuario(Long idUsuario);
    void inactivarUsuario(Long idUsuario);
    void eliminarUsuario(Long idUsuario);
}