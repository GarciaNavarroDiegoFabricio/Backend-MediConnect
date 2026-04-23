package com.Backend.MediConnect.market.domain.controller;

import com.Backend.MediConnect.market.domain.dto.RecetaDTO;
import com.Backend.MediConnect.market.domain.services.RecetaService;
import com.Backend.MediConnect.market.persistance.entity.Receta;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recetas")
public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    @PostMapping
    public Receta crearReceta(@RequestBody RecetaDTO dto) {
        return recetaService.crearReceta(dto);
    }
}