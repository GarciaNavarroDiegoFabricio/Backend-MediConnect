package com.Backend.MediConnect.clinica.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Backend.MediConnect.clinica.domain.dto.SedeRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.SedeResponseDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.ISedeService;
import com.Backend.MediConnect.clinica.domain.repository.SedeRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Sede;
import com.Backend.MediConnect.clinica.web.mapper.MantenimientoMapper;

@Service
public class SedeService implements ISedeService {

    private final SedeRepository sedeRepo;

    public SedeService(SedeRepository sedeRepo) {
        this.sedeRepo = sedeRepo;
    }

    @Override
    @Transactional
    public SedeResponseDTO registrarSede(SedeRequestDTO dto) {
        if (sedeRepo.existsByNombreSede(dto.getNombreSede())) {
            throw new RuntimeException("Ya existe una sede con ese nombre");
        }

        Sede sede = new Sede();
        sede.setNombreSede(dto.getNombreSede());
        sede.setUbicacion(dto.getUbicacion());
        sede.setTelefono(dto.getTelefono());
        sede.setActivo(true);

        return MantenimientoMapper.toSedeResponse(sedeRepo.save(sede));
    }

    @Override
    @Transactional
    public SedeResponseDTO actualizarSede(Integer id, SedeRequestDTO dto) {
        Sede sede = sedeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

        sede.setNombreSede(dto.getNombreSede());
        sede.setUbicacion(dto.getUbicacion());
        sede.setTelefono(dto.getTelefono());

        return MantenimientoMapper.toSedeResponse(sedeRepo.save(sede));
    }

    @Override
    @Transactional
    public void inactivarSede(Integer id) {
        Sede sede = sedeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));
        sede.setActivo(false);
        sedeRepo.save(sede);
    }

    @Override
    @Transactional
    public void activarSede(Integer id) {
        Sede sede = sedeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));
        sede.setActivo(true);
        sedeRepo.save(sede);
    }

    @Override
    public List<SedeResponseDTO> listarSedes() {
        return sedeRepo.findAll()
                .stream()
                .map(MantenimientoMapper::toSedeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SedeResponseDTO obtenerSede(Integer id) {
        Sede sede = sedeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));
        return MantenimientoMapper.toSedeResponse(sede);
    }
}