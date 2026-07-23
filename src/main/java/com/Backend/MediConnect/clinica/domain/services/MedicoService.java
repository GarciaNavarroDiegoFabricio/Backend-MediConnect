package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.request.MedicoComplementoRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.MedicoResponseDTO;
import com.Backend.MediConnect.clinica.domain.exception.BusinessException;
import com.Backend.MediConnect.clinica.domain.exception.ResourceNotFoundException;
import com.Backend.MediConnect.clinica.domain.interfaces.RolUsuario;
import com.Backend.MediConnect.clinica.domain.repository.*;
import com.Backend.MediConnect.clinica.persistance.entity.*;
import com.Backend.MediConnect.clinica.web.mapper.MedicoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicoService {

    private final IMedicoRepository medicoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final IPersonaRepository personaRepository;
    private final IEspecialidadRepository especialidadRepository;
    private final ISedeRepository sedeRepository;
    private final MedicoMapper medicoMapper;

    public MedicoService(IMedicoRepository medicoRepository, IUsuarioRepository usuarioRepository,
                         IPersonaRepository personaRepository, IEspecialidadRepository especialidadRepository,
                         ISedeRepository sedeRepository, MedicoMapper medicoMapper) {
        this.medicoRepository = medicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
        this.especialidadRepository = especialidadRepository;
        this.sedeRepository = sedeRepository;
        this.medicoMapper = medicoMapper;
    }

    @Transactional
    public MedicoResponseDTO completarDatos(Long idUsuario, MedicoComplementoRequestDTO request, String usuarioCreacion) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        if (!usuario.getIdRol().equals(RolUsuario.MEDICO.getId())) {
            throw new BusinessException("Este usuario no tiene el rol de Médico.");
        }

        Persona persona = personaRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Datos personales no encontrados."));

        if (medicoRepository.findByPersona_IdPersona(persona.getIdPersona()).isPresent()) {
            throw new BusinessException("Este médico ya tiene datos profesionales registrados.");
        }

        if (medicoRepository.existsByNumeroColegiatura(request.getNumeroColegiatura())) {
            throw new BusinessException("El número de colegiatura ya está registrado.");
        }

        if (usuario.getIdSede() == null) {
            throw new BusinessException("El usuario no tiene una sede asignada.");
        }

        Especialidad especialidad = especialidadRepository.findById(request.getIdEspecialidad())
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada."));

        Medico medico = Medico.builder()
                .persona(persona)
                .numeroColegiatura(request.getNumeroColegiatura())
                .especialidad(especialidad)
                .disponible(true)
                .estado("ACTIVO")
                .usuarioCreacion(usuarioCreacion)
                .build();

        medico = medicoRepository.save(medico);
        return medicoMapper.toResponse(medico);
    }

    @Transactional
    public MedicoResponseDTO actualizarSedeYEspecialidad(Long idMedico, Long idEspecialidad, Long idSede, String usuarioModificacion) {
        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado."));

        if (idEspecialidad != null) {
            Especialidad especialidad = especialidadRepository.findById(idEspecialidad)
                    .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada."));
            medico.setEspecialidad(especialidad);
            medico.setUsuarioModificacion(usuarioModificacion);
            medicoRepository.save(medico);
        }

        if (idSede != null) {
            sedeRepository.findById(idSede)
                    .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada."));

            Usuario usuario = medico.getPersona().getUsuario();
            usuario.setIdSede(idSede);
            usuario.setUsuarioModificacion(usuarioModificacion);
            usuarioRepository.save(usuario);
        }

        return medicoMapper.toResponse(medico);
    }

    @Transactional
    public MedicoResponseDTO actualizarDisponibilidad(Long idMedico, Boolean disponible, String usuarioModificacion) {
        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado."));

        medico.setDisponible(disponible);
        medico.setUsuarioModificacion(usuarioModificacion);
        medico = medicoRepository.save(medico);

        return medicoMapper.toResponse(medico);
    }

    @Transactional
    public void inactivar(Long idMedico, String usuarioModificacion) {
        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado."));

        medico.setEstado("INACTIVO");
        medico.setDisponible(false);
        medico.setUsuarioModificacion(usuarioModificacion);
        medicoRepository.save(medico);
    }

    @Transactional
    public void activar(Long idMedico, String usuarioModificacion) {
        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado."));

        medico.setEstado("ACTIVO");
        medico.setUsuarioModificacion(usuarioModificacion);
        medicoRepository.save(medico);
    }

    public MedicoResponseDTO consultarPorId(Long idMedico) {
        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado."));
        return medicoMapper.toResponse(medico);
    }

    public List<MedicoResponseDTO> listarDisponiblesPorEspecialidadYSede(Long idEspecialidad, Long idSede) {
        List<Medico> medicos;

        if (idEspecialidad != null && idSede != null) {
            medicos = medicoRepository.findByEspecialidad_IdEspecialidadAndEstado(idEspecialidad, "ACTIVO").stream()
                    .filter(m -> idSede.equals(m.getPersona().getUsuario().getIdSede()) && m.getDisponible())
                    .toList();
        } else if (idEspecialidad != null) {
            medicos = medicoRepository.findByEspecialidad_IdEspecialidadAndEstado(idEspecialidad, "ACTIVO").stream()
                    .filter(Medico::getDisponible)
                    .toList();
        } else if (idSede != null) {
            medicos = medicoRepository.findByPersona_Usuario_IdSedeAndEstado(idSede, "ACTIVO").stream()
                    .filter(Medico::getDisponible)
                    .toList();
        } else {
            medicos = medicoRepository.findByEstadoAndDisponible("ACTIVO", true);
        }

        return medicos.stream().map(medicoMapper::toResponse).toList();
    }

    public List<MedicoResponseDTO> listarTodos() {
        return medicoRepository.findAll().stream().map(medicoMapper::toResponse).toList();
    }
}