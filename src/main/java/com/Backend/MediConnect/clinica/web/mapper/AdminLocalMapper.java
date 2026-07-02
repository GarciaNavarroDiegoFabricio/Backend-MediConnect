package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.AdminLocalResponseDTO;
import com.Backend.MediConnect.clinica.persistance.entity.AdministadorLocal;

public class AdminLocalMapper {

    public static AdminLocalResponseDTO toResponse(AdministadorLocal admin) {
        if (admin == null) return null;

        AdminLocalResponseDTO dto = new AdminLocalResponseDTO();
        dto.setIdAdminLocal(admin.getIdAdminLocal());
        dto.setPrimerNombre(admin.getPrimerNombre());
        dto.setSegundoNombre(admin.getSegundoNombre());
        dto.setPrimerApellido(admin.getPrimerApellido());
        dto.setSegundoApellido(admin.getSegundoApellido());
        dto.setDni(admin.getDni());
        dto.setEstado(admin.getEstado());
        if (admin.getSede() != null) {
            dto.setIdSede(admin.getSede().getIdSede());
            dto.setNombreSede(admin.getSede().getNombreSede());
        }
        return dto;
    }
}