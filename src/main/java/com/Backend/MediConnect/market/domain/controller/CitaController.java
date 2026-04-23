package com.Backend.MediConnect.market.domain.controller;

import com.Backend.MediConnect.market.domain.dto.ReservaDTO;
import com.Backend.MediConnect.market.persistance.entity.Medico;
import com.Backend.MediConnect.market.domain.services.CitaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST encargado de manejar las solicitudes relacionadas a citas médicas.
 */
@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    //Endpoint GET para consultar las reservas de un médico específico.
    @GetMapping("/reservas/{medicoId}")
    public List<ReservaDTO> consultarReservas(@PathVariable Long medicoId) {
        Medico medico = new Medico();
        // Se crea un objeto Medico solo con el ID
        medico.setIdMedico(medicoId.intValue());

        // Se llama al servicio para obtener las reservas del médico
        return citaService.consultarReservas(medico);
    }
}
