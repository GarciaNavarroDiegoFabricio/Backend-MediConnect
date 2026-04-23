package com.Backend.MediConnect.market.domain.interfaces;

import com.Backend.MediConnect.market.domain.dto.AdminLocalResponseDTO;
import com.Backend.MediConnect.market.domain.dto.AuthResponse;
import com.Backend.MediConnect.market.domain.dto.EditarAdminLocalDTO;
import com.Backend.MediConnect.market.domain.dto.RegistroAdminLocalDTO;
import java.util.List;

public interface IAdminTotalService {
    AuthResponse crearAdminLocal(RegistroAdminLocalDTO dto);
    void eliminarAdminLocal(Integer idAdminLocal);
    AdminLocalResponseDTO editarAdminLocal(Integer idAdminLocal, EditarAdminLocalDTO dto);
    List<AdminLocalResponseDTO> consultarAdminLocales();
}