package com.Backend.MediConnect.market.web.controller;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Backend.MediConnect.market.domain.service.HorarioService;
import com.Backend.MediConnect.market.domain.service.MedicoService;
import com.Backend.MediConnect.market.persistance.entity.Horario;
import com.Backend.MediConnect.market.persistance.entity.Medico;

@RestController
@RequestMapping("/api/gestion")
public class GestionMedicoController {

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private HorarioService horarioService;

    // 1. GESTIONAR MEDICO: Crear o Editar
    @PostMapping("/medico")
    public ResponseEntity<Medico> save(@RequestBody Medico medico) {
        return new ResponseEntity<>(medicoService.guardarMedico(medico), HttpStatus.CREATED);
    }

    @GetMapping("/medico/{id}")
    public ResponseEntity<Medico> getMedico(@PathVariable Integer id) {
        return medicoService.obtenerPorId(id)
                .map(medico -> new ResponseEntity<>(medico, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 2. CAMBIAR DISPONIBILIDAD
    @PatchMapping("/medico/{id}/disponibilidad")
    public ResponseEntity<Medico> toggleDisponibilidad(@PathVariable Integer id) {
        Medico medico = medicoService.cambiarDisponibilidad(id);
        return (medico != null) ? new ResponseEntity<>(medico, HttpStatus.OK) 
                                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // 3. REPROGRAMAR HORARIO
    // Se envía el ID del horario y las nuevas horas por parámetros
    @PutMapping("/horario/{id}/reprogramar")
    public ResponseEntity<Horario> reprogramar(@PathVariable Integer id, 
                                               @RequestParam String inicio, 
                                               @RequestParam String fin) {
        
        LocalTime horaInicio = LocalTime.parse(inicio);
        LocalTime horaFin = LocalTime.parse(fin);
        
        Horario horario = horarioService.reprogramar(id, horaInicio, horaFin);
        return (horario != null) ? new ResponseEntity<>(horario, HttpStatus.OK) 
                                 : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}