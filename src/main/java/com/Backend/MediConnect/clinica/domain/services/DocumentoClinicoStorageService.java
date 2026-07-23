package com.Backend.MediConnect.clinica.domain.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.Backend.MediConnect.clinica.domain.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class DocumentoClinicoStorageService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.folder-documentos-clinicos}")
    private String carpeta;

    public DocumentoClinicoStorageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map<String, String> subir(MultipartFile archivo, Long idPaciente) {
        if (archivo == null || archivo.isEmpty()) {
            throw new BusinessException("Debe seleccionar un archivo para subir.");
        }

        String contentType = archivo.getContentType();
        if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
            throw new BusinessException("El archivo debe ser una imagen o un PDF válido.");
        }

        String publicId = "paciente_" + idPaciente + "_" + UUID.randomUUID();

        try {
            Map<String, Object> opciones = ObjectUtils.asMap(
                    "folder", carpeta,
                    "public_id", publicId,
                    "overwrite", true,
                    "resource_type", "auto"
            );

            Map resultado = cloudinary.uploader().upload(archivo.getBytes(), opciones);
            String url = (String) resultado.get("secure_url");

            return Map.of("url", url, "publicId", publicId);
        } catch (IOException e) {
            throw new BusinessException("Error al subir el documento a Cloudinary.");
        }
    }

    public void eliminar(String publicId) {
        try {
            String rutaCompleta = carpeta + "/" + publicId;
            cloudinary.uploader().destroy(rutaCompleta, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new BusinessException("Error al eliminar el documento de Cloudinary.");
        }
    }
}