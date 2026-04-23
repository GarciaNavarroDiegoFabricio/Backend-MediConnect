package com.Backend.MediConnect.market.domain.controller;

import com.Backend.MediConnect.market.domain.dto.RecetaDTO;
import com.Backend.MediConnect.market.domain.services.RecetaService;
import com.Backend.MediConnect.market.persistance.entity.Receta;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas a recetas médicas.
 */
@RestController
@RequestMapping("/recetas")
public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    //Endpoint POST para crear una nueva receta médica.
    @PostMapping
    public Receta crearReceta(@RequestBody RecetaDTO dto) {
        // Llama al servicio para crear y guardar la receta
        return recetaService.crearReceta(dto);
    }
}