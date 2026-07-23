package com.Backend.MediConnect.clinica.domain.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.Backend.MediConnect.clinica.domain.exception.BusinessException;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.repository.IPersonaRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Persona;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class FotoPerfilService {

    private final Cloudinary cloudinary;
    private final IPersonaRepository personaRepository;

    @Value("${cloudinary.folder-perfiles}")
    private String carpeta;

    public FotoPerfilService(Cloudinary cloudinary, IPersonaRepository personaRepository) {
        this.cloudinary = cloudinary;
        this.personaRepository = personaRepository;
    }

    @Transactional
    public String actualizarFotoPorIdUsuario(Long idUsuario, MultipartFile archivo) {
        Persona persona = personaRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Datos personales no encontrados."));

        return subirYActualizar(persona, archivo);
    }

    @Transactional
    public String actualizarFotoPorDni(String dni, MultipartFile archivo) {
        Persona persona = personaRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("No existe una persona con ese DNI."));

        return subirYActualizar(persona, archivo);
    }

    @Transactional
    public void eliminarFotoPorIdUsuario(Long idUsuario) {
        Persona persona = personaRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Datos personales no encontrados."));

        eliminarYLimpiar(persona);
    }

    @Transactional
    public void eliminarFotoPorDni(String dni) {
        Persona persona = personaRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("No existe una persona con ese DNI."));

        eliminarYLimpiar(persona);
    }

    private String subirYActualizar(Persona persona, MultipartFile archivo) {
        if (persona.getFotoPerfilPublicId() != null) {
            eliminarDeCloudinary(persona.getFotoPerfilPublicId());
        }

        String publicId = persona.getDni() + "_" + UUID.randomUUID();
        String url = subirImagen(archivo, publicId);

        persona.setFotoPerfil(url);
        persona.setFotoPerfilPublicId(publicId);
        personaRepository.save(persona);

        return url;
    }

    private void eliminarYLimpiar(Persona persona) {
        if (persona.getFotoPerfilPublicId() == null) {
            throw new BusinessException("Este usuario no tiene una foto de perfil registrada.");
        }

        eliminarDeCloudinary(persona.getFotoPerfilPublicId());

        persona.setFotoPerfil(null);
        persona.setFotoPerfilPublicId(null);
        personaRepository.save(persona);
    }

    private String subirImagen(MultipartFile archivo, String publicId) {
        if (archivo == null || archivo.isEmpty()) {
            throw new BusinessException("Debe seleccionar una imagen para subir.");
        }

        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException("El archivo debe ser una imagen válida.");
        }

        try {
            Map<String, Object> opciones = ObjectUtils.asMap(
                    "folder", carpeta,
                    "public_id", publicId,
                    "overwrite", true,
                    "resource_type", "image"
            );

            Map resultado = cloudinary.uploader().upload(archivo.getBytes(), opciones);
            return (String) resultado.get("secure_url");
        } catch (IOException e) {
            throw new BusinessException("Error al subir la imagen a Cloudinary.");
        }
    }

    private void eliminarDeCloudinary(String publicId) {
        try {
            String rutaCompleta = carpeta + "/" + publicId;
            cloudinary.uploader().destroy(rutaCompleta, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new BusinessException("Error al eliminar la imagen anterior de Cloudinary.");
        }
    }
}