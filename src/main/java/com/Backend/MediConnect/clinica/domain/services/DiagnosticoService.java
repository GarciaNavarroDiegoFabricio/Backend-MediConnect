package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.repository.ConsultaRepository;
import com.Backend.MediConnect.clinica.domain.repository.DiagnosticoRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Consulta;
import com.Backend.MediConnect.clinica.persistance.entity.Diagnostico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DiagnosticoService {

    @Autowired
    private DiagnosticoRepository diagnosticoRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    public Diagnostico registrarDiagnostico(Integer consultaId, String descripcion) {

        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setDescripcion(descripcion);
        diagnostico.setFechaCreacion(LocalDateTime.now());
        diagnostico.setConsulta(consulta);

        return diagnosticoRepository.save(diagnostico);
    }
}