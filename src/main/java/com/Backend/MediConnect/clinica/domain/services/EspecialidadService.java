package com.Backend.MediConnect.clinica.domain.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.Backend.MediConnect.clinica.domain.dto.request.EspecialidadRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.EspecialidadPublicaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.EspecialidadResponseDTO;
import com.Backend.MediConnect.clinica.domain.exception.BusinessException;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.repository.IEspecialidadRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Especialidad;
import com.Backend.MediConnect.clinica.web.mapper.EspecialidadMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EspecialidadService {

    private final IEspecialidadRepository especialidadRepository;
    private final EspecialidadMapper especialidadMapper;
    private final Cloudinary cloudinary;

    @Value("${cloudinary.folder-especialidades}")
    private String carpeta;

    public EspecialidadService(IEspecialidadRepository especialidadRepository, EspecialidadMapper especialidadMapper, Cloudinary cloudinary) {
        this.especialidadRepository = especialidadRepository;
        this.especialidadMapper = especialidadMapper;
        this.cloudinary = cloudinary;
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
    public String actualizarFoto(Long idEspecialidad, MultipartFile archivo, String usuarioModificacion) {
        Especialidad especialidad = especialidadRepository.findById(idEspecialidad)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada."));

        if (archivo == null || archivo.isEmpty()) {
            throw new BusinessException("Debe seleccionar una imagen para subir.");
        }

        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException("El archivo debe ser una imagen válida.");
        }

        if (especialidad.getFotoPublicId() != null) {
            eliminarDeCloudinary(especialidad.getFotoPublicId());
        }

        String publicId = "especialidad_" + idEspecialidad + "_" + UUID.randomUUID();

        try {
            Map<String, Object> opciones = ObjectUtils.asMap(
                    "folder", carpeta,
                    "public_id", publicId,
                    "overwrite", true,
                    "resource_type", "image"
            );

            Map resultado = cloudinary.uploader().upload(archivo.getBytes(), opciones);
            String url = (String) resultado.get("secure_url");

            especialidad.setFoto(url);
            especialidad.setFotoPublicId(publicId);
            especialidad.setUsuarioModificacion(usuarioModificacion);
            especialidadRepository.save(especialidad);

            return url;
        } catch (IOException e) {
            throw new BusinessException("Error al subir la imagen a Cloudinary.");
        }
    }

    @Transactional
    public void eliminarFoto(Long idEspecialidad, String usuarioModificacion) {
        Especialidad especialidad = especialidadRepository.findById(idEspecialidad)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada."));

        if (especialidad.getFotoPublicId() == null) {
            throw new BusinessException("Esta especialidad no tiene una foto registrada.");
        }

        eliminarDeCloudinary(especialidad.getFotoPublicId());

        especialidad.setFoto(null);
        especialidad.setFotoPublicId(null);
        especialidad.setUsuarioModificacion(usuarioModificacion);
        especialidadRepository.save(especialidad);
    }

    private void eliminarDeCloudinary(String publicId) {
        try {
            String rutaCompleta = carpeta + "/" + publicId;
            cloudinary.uploader().destroy(rutaCompleta, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new BusinessException("Error al eliminar la imagen anterior de Cloudinary.");
        }
    }

    @Transactional
    public void eliminar(Long idEspecialidad) {
        Especialidad especialidad = especialidadRepository.findById(idEspecialidad)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada."));

        if (especialidad.getFotoPublicId() != null) {
            eliminarDeCloudinary(especialidad.getFotoPublicId());
        }

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