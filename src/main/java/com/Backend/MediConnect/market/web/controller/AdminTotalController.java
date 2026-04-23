package com.Backend.MediConnect.market.web.controller;

import com.Backend.MediConnect.market.domain.dto.*;
import com.Backend.MediConnect.market.domain.interfaces.IAdminTotalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin-total")
public class AdminTotalController {

    private final IAdminTotalService adminTotalService;

    public AdminTotalController(IAdminTotalService adminTotalService) {
        this.adminTotalService = adminTotalService;
    }

    @PostMapping("/admin-local")
    public ResponseEntity<AuthResponse> crearAdminLocal(@RequestBody RegistroAdminLocalDTO dto) {
        return ResponseEntity.ok(adminTotalService.crearAdminLocal(dto));
    }

    @DeleteMapping("/admin-local/{id}")
    public ResponseEntity<Void> eliminarAdminLocal(@PathVariable Integer id) {
        adminTotalService.eliminarAdminLocal(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin-local/{id}")
    public ResponseEntity<AdminLocalResponseDTO> editarAdminLocal(@PathVariable Integer id,
                                                                  @RequestBody EditarAdminLocalDTO dto) {
        return ResponseEntity.ok(adminTotalService.editarAdminLocal(id, dto));
    }

    @GetMapping("/admin-locales")
    public ResponseEntity<List<AdminLocalResponseDTO>> consultarAdminLocales() {
        return ResponseEntity.ok(adminTotalService.consultarAdminLocales());
    }
}