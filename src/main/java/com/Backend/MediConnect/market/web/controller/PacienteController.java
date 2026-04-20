package com.Backend.MediConnect.market.web.controller;

import com.Backend.MediConnect.market.domain.dto.cancelarCitaDTO;
import com.Backend.MediConnect.market.domain.dto.consultarCitaDTO;
import com.Backend.MediConnect.market.domain.dto.crearCitaDTO;
import com.Backend.MediConnect.market.domain.service.CitaService;
import com.Backend.MediConnect.market.persistance.entity.Cita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private CitaService citaService;

    @PostMapping("/crearCita")
    public ResponseEntity<consultarCitaDTO> crearCita(@RequestBody crearCitaDTO request){
        return ResponseEntity.ok(citaService.crearCita(request));
    }

    @GetMapping("/consultarCita/{id}")
    public ResponseEntity<List<consultarCitaDTO>> consultarCita(@PathVariable Integer id){
        List<consultarCitaDTO> citasPaciente = citaService.obtenerCitasPorPaciente(id);
        return ResponseEntity.ok(citasPaciente);
    }

    @GetMapping("/cancelarCita/{idCita}/{idPaciente}")
    public ResponseEntity<cancelarCitaDTO> cancelarCita(@PathVariable Integer idCita, @PathVariable Integer idPaciente){
        cancelarCitaDTO citaCancelada = citaService.cancelarCita(idCita, idPaciente);
        return ResponseEntity.ok(citaCancelada);
    }



}
