package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.request.EspecialidadRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.EspecialidadPublicaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.EspecialidadResponseDTO;
import com.Backend.MediConnect.clinica.domain.exception.BusinessException;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.repository.IEspecialidadRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Especialidad;
import com.Backend.MediConnect.clinica.web.mapper.EspecialidadMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EspecialidadService {

    private final IEspecialidadRepository especialidadRepository;
    private final EspecialidadMapper especialidadMapper;

    public EspecialidadService(IEspecialidadRepository especialidadRepository, EspecialidadMapper especialidadMapper) {
        this.especialidadRepository = especialidadRepository;
        this.especialidadMapper = especialidadMapper;
    }

    @Transactional
    public EspecialidadResponseDTO crear(EspecialidadRequestDTO request, String usuarioCreacion) {
        if (especialidadRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new BusinessException("Ya existe una especialidad con ese nombre.");
        }

        Especialidad especialidad = Especialidad.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .usuarioCreacion(usuarioCreacion)
                .build();

        especialidad = especialidadRepository.save(especialidad);
        return especialidadMapper.toResponse(especialidad);
    }

    @Transactional
    public EspecialidadResponseDTO actualizar(Long idEspecialidad, EspecialidadRequestDTO request, String usuarioModificacion) {
        Especialidad especialidad = especialidadRepository.findById(idEspecialidad)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada."));

        if (request.getNombre() != null) especialidad.setNombre(request.getNombre());
        if (request.getDescripcion() != null) especialidad.setDescripcion(request.getDescripcion());
        especialidad.setUsuarioModificacion(usuarioModificacion);

        especialidad = especialidadRepository.save(especialidad);
        return especialidadMapper.toResponse(especialidad);
    }

    @Transactional
    public void eliminar(Long idEspecialidad) {
        Especialidad especialidad = especialidadRepository.findById(idEspecialidad)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada."));

        especialidadRepository.delete(especialidad);
    }

    public EspecialidadResponseDTO consultarPorId(Long idEspecialidad) {
        Especialidad especialidad = especialidadRepository.findById(idEspecialidad)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada."));
        return especialidadMapper.toResponse(especialidad);
    }

    public List<EspecialidadResponseDTO> listar() {
        return especialidadRepository.findAll().stream()
                .map(especialidadMapper::toResponse)
                .toList();
    }

    public EspecialidadPublicaResponseDTO consultarPorIdPublico(Long idEspecialidad) {
        Especialidad especialidad = especialidadRepository.findById(idEspecialidad)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada."));
        return especialidadMapper.toPublicaResponse(especialidad);
    }

    public List<EspecialidadPublicaResponseDTO> listarPublico() {
        return especialidadRepository.findAll().stream()
                .map(especialidadMapper::toPublicaResponse)
                .toList();
    }
}