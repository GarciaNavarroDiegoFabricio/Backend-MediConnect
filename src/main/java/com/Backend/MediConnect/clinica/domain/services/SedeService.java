package com.Backend.MediConnect.clinica.domain.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.Backend.MediConnect.clinica.domain.dto.request.SedeRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.SedeUpdateRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.SedePublicaResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.SedeResponseDTO;
import com.Backend.MediConnect.clinica.domain.exception.BusinessException;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.repository.ISedeRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Sede;
import com.Backend.MediConnect.clinica.web.mapper.SedeMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SedeService {

    private final ISedeRepository sedeRepository;
    private final SedeMapper sedeMapper;
    private final Cloudinary cloudinary;

    @Value("${cloudinary.folder-sedes}")
    private String carpeta;

    public SedeService(ISedeRepository sedeRepository, SedeMapper sedeMapper, Cloudinary cloudinary) {
        this.sedeRepository = sedeRepository;
        this.sedeMapper = sedeMapper;
        this.cloudinary = cloudinary;
    }

    @Transactional
    public SedeResponseDTO crear(SedeRequestDTO request, String usuarioCreacion) {
        if (sedeRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new BusinessException("Ya existe una sede con ese nombre.");
        }

        Sede sede = Sede.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .estado("ACTIVO")
                .usuarioCreacion(usuarioCreacion)
                .build();

        sede = sedeRepository.save(sede);
        return sedeMapper.toResponse(sede);
    }

    @Transactional
    public SedeResponseDTO actualizar(Long idSede, SedeUpdateRequestDTO request, String usuarioModificacion) {
        Sede sede = sedeRepository.findById(idSede)
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada."));

        if (request.getNombre() != null) sede.setNombre(request.getNombre());
        if (request.getDescripcion() != null) sede.setDescripcion(request.getDescripcion());
        sede.setUsuarioModificacion(usuarioModificacion);

        sede = sedeRepository.save(sede);
        return sedeMapper.toResponse(sede);
    }

    @Transactional
    public String actualizarFoto(Long idSede, MultipartFile archivo, String usuarioModificacion) {
        Sede sede = sedeRepository.findById(idSede)
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada."));

        if (archivo == null || archivo.isEmpty()) {
            throw new BusinessException("Debe seleccionar una imagen para subir.");
        }

        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException("El archivo debe ser una imagen válida.");
        }

        if (sede.getFotoPublicId() != null) {
            eliminarDeCloudinary(sede.getFotoPublicId());
        }

        String publicId = "sede_" + idSede + "_" + UUID.randomUUID();

        try {
            Map<String, Object> opciones = ObjectUtils.asMap(
                    "folder", carpeta,
                    "public_id", publicId,
                    "overwrite", true,
                    "resource_type", "image"
            );

            Map resultado = cloudinary.uploader().upload(archivo.getBytes(), opciones);
            String url = (String) resultado.get("secure_url");

            sede.setFoto(url);
            sede.setFotoPublicId(publicId);
            sede.setUsuarioModificacion(usuarioModificacion);
            sedeRepository.save(sede);

            return url;
        } catch (IOException e) {
            throw new BusinessException("Error al subir la imagen a Cloudinary.");
        }
    }

    @Transactional
    public void eliminarFoto(Long idSede, String usuarioModificacion) {
        Sede sede = sedeRepository.findById(idSede)
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada."));

        if (sede.getFotoPublicId() == null) {
            throw new BusinessException("Esta sede no tiene una foto registrada.");
        }

        eliminarDeCloudinary(sede.getFotoPublicId());

        sede.setFoto(null);
        sede.setFotoPublicId(null);
        sede.setUsuarioModificacion(usuarioModificacion);
        sedeRepository.save(sede);
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
    public void inactivar(Long idSede, String usuarioModificacion) {
        Sede sede = sedeRepository.findById(idSede)
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada."));

        sede.setEstado("INACTIVO");
        sede.setUsuarioModificacion(usuarioModificacion);
        sedeRepository.save(sede);
    }

    @Transactional
    public void activar(Long idSede, String usuarioModificacion) {
        Sede sede = sedeRepository.findById(idSede)
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada."));

        sede.setEstado("ACTIVO");
        sede.setUsuarioModificacion(usuarioModificacion);
        sedeRepository.save(sede);
    }

    @Transactional
    public void eliminar(Long idSede) {
        Sede sede = sedeRepository.findById(idSede)
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada."));

        if (sede.getFotoPublicId() != null) {
            eliminarDeCloudinary(sede.getFotoPublicId());
        }

        sedeRepository.delete(sede);
    }

    public SedeResponseDTO consultarPorId(Long idSede) {
        Sede sede = sedeRepository.findById(idSede)
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada."));
        return sedeMapper.toResponse(sede);
    }

    public List<SedeResponseDTO> listar() {
        return sedeRepository.findAll().stream()
                .map(sedeMapper::toResponse)
                .toList();
    }

    public SedePublicaResponseDTO consultarPorIdPublico(Long idSede) {
        Sede sede = sedeRepository.findById(idSede)
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada."));
        return sedeMapper.toPublicaResponse(sede);
    }

    public List<SedePublicaResponseDTO> listarPublico() {
        return sedeRepository.findAll().stream()
                .filter(sede -> "ACTIVO".equals(sede.getEstado()))
                .map(sedeMapper::toPublicaResponse)
                .toList();
    }
}