package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.request.PacienteComplementoRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.request.PacienteContactoUpdateDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.PacienteResponseDTO;
import com.Backend.MediConnect.clinica.domain.exception.BusinessException;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.interfaces.RolUsuario;
import com.Backend.MediConnect.clinica.domain.repository.IPacienteRepository;
import com.Backend.MediConnect.clinica.domain.repository.IPersonaRepository;
import com.Backend.MediConnect.clinica.domain.repository.IUsuarioRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Paciente;
import com.Backend.MediConnect.clinica.persistance.entity.Persona;
import com.Backend.MediConnect.clinica.persistance.entity.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;

@Service
public class PacienteService {

    private final IPacienteRepository pacienteRepository;
    private final IUsuarioRepository usuarioRepository;
    private final IPersonaRepository personaRepository;

    public PacienteService(IPacienteRepository pacienteRepository, IUsuarioRepository usuarioRepository,
                           IPersonaRepository personaRepository) {
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
    }

    @Transactional
    public PacienteResponseDTO completarDatos(Long idUsuario, PacienteComplementoRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        if (!usuario.getIdRol().equals(RolUsuario.PACIENTE.getId())) {
            throw new BusinessException("Este usuario no tiene el rol de Paciente.");
        }

        Persona persona = personaRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Datos personales no encontrados."));

        if (pacienteRepository.findByPersona_IdPersona(persona.getIdPersona()).isPresent()) {
            throw new BusinessException("Este paciente ya tiene datos de contacto registrados.");
        }

        Paciente paciente = Paciente.builder()
                .persona(persona)
                .codigoHistoriaClinica(generarCodigoHistoriaClinica())
                .telefono(request.getTelefono())
                .contactoEmergenciaNombre(request.getContactoEmergenciaNombre())
                .contactoEmergenciaTelefono(request.getContactoEmergenciaTelefono())
                .contactoEmergenciaParentesco(request.getContactoEmergenciaParentesco())
                .build();

        paciente = pacienteRepository.save(paciente);

        return toResponse(paciente);
    }

    private String generarCodigoHistoriaClinica() {
        String anio = String.valueOf(Year.now().getValue());
        long correlativo = pacienteRepository.count() + 1;
        String codigo;

        do {
            codigo = "HC-" + anio + "-" + String.format("%06d", correlativo);
            correlativo++;
        } while (pacienteRepository.existsByCodigoHistoriaClinica(codigo));

        return codigo;
    }

    @Transactional
    public PacienteResponseDTO actualizarContacto(Long idUsuario, PacienteContactoUpdateDTO request) {
        Persona persona = personaRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Datos personales no encontrados."));

        Paciente paciente = pacienteRepository.findByPersona_IdPersona(persona.getIdPersona())
                .orElseThrow(() -> new ResourceNotFoundException("Datos de paciente no encontrados."));

        if (request.getTelefono() != null) paciente.setTelefono(request.getTelefono());
        if (request.getContactoEmergenciaNombre() != null) paciente.setContactoEmergenciaNombre(request.getContactoEmergenciaNombre());
        if (request.getContactoEmergenciaTelefono() != null) paciente.setContactoEmergenciaTelefono(request.getContactoEmergenciaTelefono());
        if (request.getContactoEmergenciaParentesco() != null) paciente.setContactoEmergenciaParentesco(request.getContactoEmergenciaParentesco());
        if (request.getDireccion() != null) persona.setDireccion(request.getDireccion());

        pacienteRepository.save(paciente);
        personaRepository.save(persona);

        return toResponse(paciente);
    }

    public PacienteResponseDTO consultarPorIdUsuario(Long idUsuario) {
        Persona persona = personaRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Datos personales no encontrados."));

        Paciente paciente = pacienteRepository.findByPersona_IdPersona(persona.getIdPersona())
                .orElseThrow(() -> new ResourceNotFoundException("Datos de paciente no encontrados."));

        return toResponse(paciente);
    }

    public List<PacienteResponseDTO> buscar(String termino) {
        return personaRepository.findAll().stream()
                .filter(p -> coincide(p, termino))
                .map(p -> pacienteRepository.findByPersona_IdPersona(p.getIdPersona()).orElse(null))
                .filter(pac -> pac != null)
                .map(this::toResponse)
                .toList();
    }

    private boolean coincide(Persona persona, String termino) {
        String t = termino.toLowerCase();
        return safe(persona.getDni()).toLowerCase().contains(t)
                || safe(persona.getNombres()).toLowerCase().contains(t)
                || safe(persona.getApellidoPaterno()).toLowerCase().contains(t)
                || safe(persona.getApellidoMaterno()).toLowerCase().contains(t)
                || pacienteRepository.findByPersona_IdPersona(persona.getIdPersona())
                .map(pac -> pac.getCodigoHistoriaClinica().toLowerCase().contains(t))
                .orElse(false);
    }

    private PacienteResponseDTO toResponse(Paciente paciente) {
        Persona persona = paciente.getPersona();
        return PacienteResponseDTO.builder()
                .idPaciente(paciente.getIdPaciente())
                .idUsuario(persona.getUsuario().getIdUsuario())
                .dni(persona.getDni())
                .nombres(persona.getNombres())
                .apellidoPaterno(persona.getApellidoPaterno())
                .apellidoMaterno(persona.getApellidoMaterno())
                .correo(persona.getUsuario().getCorreo())
                .fotoPerfil(persona.getFotoPerfil())
                .codigoHistoriaClinica(paciente.getCodigoHistoriaClinica())
                .telefono(paciente.getTelefono())
                .direccion(persona.getDireccion())
                .contactoEmergenciaNombre(paciente.getContactoEmergenciaNombre())
                .contactoEmergenciaTelefono(paciente.getContactoEmergenciaTelefono())
                .contactoEmergenciaParentesco(paciente.getContactoEmergenciaParentesco())
                .build();
    }

    private String safe(String valor) {
        return valor != null ? valor : "";
    }
}