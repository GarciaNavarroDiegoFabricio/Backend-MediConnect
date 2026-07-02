package com.Backend.MediConnect.clinica.domain.interfaces;

import java.util.List;

import com.Backend.MediConnect.clinica.domain.dto.AdminLocalResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.EditarAdminLocalDTO;
import com.Backend.MediConnect.clinica.domain.dto.RegistroAdminLocalDTO;

public interface IAdminTotalService {
    AdminLocalResponseDTO crearAdminLocal(RegistroAdminLocalDTO dto);
    void eliminarAdminLocal(Integer idAdminLocal);
    AdminLocalResponseDTO editarAdminLocal(Integer idAdminLocal, EditarAdminLocalDTO dto);
    List<AdminLocalResponseDTO> consultarAdminLocales();
    void cambiarEstadoUsuarioPorDni(String dni, boolean activo);
}