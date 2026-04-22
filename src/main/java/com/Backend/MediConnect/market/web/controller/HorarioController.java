package com.Backend.MediConnect.market.web.controller;

import com.Backend.MediConnect.market.domain.dto.consultarHorarioDTO;
import com.Backend.MediConnect.market.domain.dto.crearHorarioDTO;
import com.Backend.MediConnect.market.domain.service.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @PostMapping("/crearHorario")
    public ResponseEntity<consultarHorarioDTO> crearHorario(@RequestBody crearHorarioDTO request) {
        return ResponseEntity.ok(horarioService.crearHorario(request));
    }

    @GetMapping("/cancelarHorario/{id}")
    public ResponseEntity<consultarHorarioDTO> cancelarHorario(@PathVariable Integer id) {
        consultarHorarioDTO horarioCancelado = horarioService.cancelarHorario(id);
        return ResponseEntity.ok(horarioCancelado);
    }

    @GetMapping("/bloquearHorario/{id}")
    public ResponseEntity<consultarHorarioDTO> bloquearHorario(@PathVariable Integer id) {
        consultarHorarioDTO horarioBloqueado = horarioService.bloquearHorario(id);
        return ResponseEntity.ok(horarioBloqueado);
    }
}
