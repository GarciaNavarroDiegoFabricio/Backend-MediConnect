package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.dto.*;
import com.Backend.MediConnect.clinica.domain.repository.ConsultaRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Consulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HistorialService {

    @Autowired
    private ConsultaRepository consultaRepository;

    public List<HistorialClinicoAgrupadoDTO> obtenerHistorial(Integer pacienteId) {

        List<Consulta> consultas = consultaRepository.findByPaciente_IdPaciente(pacienteId);

        // 1. Agrupar por FECHA
        Map<LocalDate, List<Consulta>> porFecha = consultas.stream()
                .collect(Collectors.groupingBy(c -> c.getHoraInicio().toLocalDate()));

        // 2. Convertir a DTO agrupado
        List<HistorialClinicoAgrupadoDTO> resultado = new ArrayList<>();

        for (Map.Entry<LocalDate, List<Consulta>> entry : porFecha.entrySet()) {

            HistorialClinicoAgrupadoDTO grupo = new HistorialClinicoAgrupadoDTO();
            grupo.setFecha(entry.getKey());

            List<AtencionMedicaDTO> atenciones = entry.getValue()
                    .stream()
                    .map(c -> {

                        AtencionMedicaDTO dto = new AtencionMedicaDTO();

                        dto.setMedico(
                                c.getMedico().getPrimerNombre() + " " +
                                        c.getMedico().getPrimerApellido());

                        dto.setHoraConsulta(c.getHoraInicio());
                        dto.setEstadoConsulta(c.getEstado());

                        if (c.getDiagnostico() != null) {
                            dto.setDiagnostico(c.getDiagnostico().getDescripcion());
                        } else {
                            dto.setDiagnostico("SIN DIAGNÓSTICO");
                        }

                        return dto;
                    })
                    .toList();

            grupo.setAtenciones(atenciones);
            resultado.add(grupo);
        }

        // ordenar por fecha descendente (más reciente primero)
        resultado.sort((a, b) -> b.getFecha().compareTo(a.getFecha()));

        return resultado;
    }
}