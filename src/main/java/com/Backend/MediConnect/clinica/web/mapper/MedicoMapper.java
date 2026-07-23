package com.Backend.MediConnect.clinica.web.mapper;

import com.Backend.MediConnect.clinica.domain.dto.response.MedicoResponseDTO;
import com.Backend.MediConnect.clinica.domain.repository.ISedeRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Medico;
import com.Backend.MediConnect.clinica.persistance.entity.Sede;
import org.springframework.stereotype.Component;

@Component
public class MedicoMapper {

    private final ISedeRepository sedeRepository;

    public MedicoMapper(ISedeRepository sedeRepository) {
        this.sedeRepository = sedeRepository;
    }

    public MedicoResponseDTO toResponse(Medico medico) {
        Long idSede = medico.getPersona().getUsuario().getIdSede();
        Sede sede = idSede != null ? sedeRepository.findById(idSede).orElse(null) : null;

        return MedicoResponseDTO.builder()
                .idMedico(medico.getIdMedico())
                .idUsuario(medico.getPersona().getUsuario().getIdUsuario())
                .dni(medico.getPersona().getDni())
                .nombres(medico.getPersona().getNombres())
                .apellidoPaterno(medico.getPersona().getApellidoPaterno())
                .apellidoMaterno(medico.getPersona().getApellidoMaterno())
                .correo(medico.getPersona().getUsuario().getCorreo())
                .fotoPerfil(medico.getPersona().getFotoPerfil())
                .numeroColegiatura(medico.getNumeroColegiatura())
                .idEspecialidad(medico.getEspecialidad().getIdEspecialidad())
                .nombreEspecialidad(medico.getEspecialidad().getNombre())
                .idSede(idSede)
                .nombreSede(sede != null ? sede.getNombre() : null)
                .disponible(medico.getDisponible())
                .estado(medico.getEstado())
                .build();
    }
}