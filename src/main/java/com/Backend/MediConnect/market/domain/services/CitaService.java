package com.Backend.MediConnect.market.domain.services;

import com.Backend.MediConnect.market.domain.dto.ReservaDTO;
import com.Backend.MediConnect.market.persistance.entity.*;
import com.Backend.MediConnect.market.domain.repository.CitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**Servicio encargado de la lógica de negocio relacionada a las citas médicas.
 *
 */
@Service
public class CitaService {

    private final CitaRepository citaRepository;

    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    //Consulta las reservas de citas relacionadas a un médico específico.
    public List<ReservaDTO> consultarReservas(Medico medico) {
        // Obtiene las citas del médico desde la base de datos
        List<Cita> citas = citaRepository.findByMedico(medico);
        return citas.stream()
                .map(c -> new ReservaDTO(
                        c.getIdCita().longValue(),
                        c.getPaciente().getPrimerNombre() + " " + c.getPaciente().getPrimerApellido(),
                        c.getFecha(),
                        c.getHora(),
                        c.getEstado(),
                        c.getEspecialidad()
                ))
                .collect(Collectors.toList());
    }
}
