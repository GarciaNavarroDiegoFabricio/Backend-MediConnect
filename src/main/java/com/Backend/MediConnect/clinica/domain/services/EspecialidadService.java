package com.Backend.MediConnect.clinica.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Backend.MediConnect.clinica.domain.dto.EspecialidadRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.EspecialidadResponseDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.IEspecialidadService;
import com.Backend.MediConnect.clinica.domain.repository.EspecialidadRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Especialidad;
import com.Backend.MediConnect.clinica.web.mapper.MantenimientoMapper;

@Service
public class EspecialidadService implements IEspecialidadService {

    private final EspecialidadRepository especialidadRepo;

    public EspecialidadService(EspecialidadRepository especialidadRepo) {
        this.especialidadRepo = especialidadRepo;
    }

    @Override
    @Transactional
    public EspecialidadResponseDTO registrarEspecialidad(EspecialidadRequestDTO dto) {
        if (especialidadRepo.existsByNombreEspecialidad(dto.getNombreEspecialidad())) {
            throw new RuntimeException("Ya existe una especialidad con ese nombre");
        }

        Especialidad especialidad = new Especialidad();
        especialidad.setNombreEspecialidad(dto.getNombreEspecialidad());
        especialidad.setActivo(true);

        return MantenimientoMapper.toEspecialidadResponse(especialidadRepo.save(especialidad));
    }

    @Override
    @Transactional
    public EspecialidadResponseDTO actualizarEspecialidad(Integer id, EspecialidadRequestDTO dto) {
        Especialidad especialidad = especialidadRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        especialidad.setNombreEspecialidad(dto.getNombreEspecialidad());

        return MantenimientoMapper.toEspecialidadResponse(especialidadRepo.save(especialidad));
    }

    @Override
    @Transactional
    public void inactivarEspecialidad(Integer id) {
        Especialidad especialidad = especialidadRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        especialidad.setActivo(false);
        especialidadRepo.save(especialidad);
    }

    @Override
    @Transactional
    public void activarEspecialidad(Integer id) {
        Especialidad especialidad = especialidadRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        especialidad.setActivo(true);
        especialidadRepo.save(especialidad);
    }

    @Override
    public List<EspecialidadResponseDTO> listarEspecialidades() {
        return especialidadRepo.findAll()
                .stream()
                .map(MantenimientoMapper::toEspecialidadResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EspecialidadResponseDTO obtenerEspecialidad(Integer id) {
        Especialidad especialidad = especialidadRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        return MantenimientoMapper.toEspecialidadResponse(especialidad);
    }
}