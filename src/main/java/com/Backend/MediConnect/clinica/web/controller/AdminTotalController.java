package com.Backend.MediConnect.clinica.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Backend.MediConnect.clinica.domain.dto.AdminLocalResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.EditarAdminLocalDTO;
import com.Backend.MediConnect.clinica.domain.dto.RegistroAdminLocalDTO;
import com.Backend.MediConnect.clinica.domain.dto.ActualizarEstadoUsuarioDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.IAdminTotalService;

import java.util.List;

@RestController
@RequestMapping("/api/admin-total")
public class AdminTotalController {

    private final IAdminTotalService adminTotalService;

    public AdminTotalController(IAdminTotalService adminTotalService) {
        this.adminTotalService = adminTotalService;
    }

    @PostMapping("/admin-local")
    public ResponseEntity<AdminLocalResponseDTO> crearAdminLocal(@RequestBody RegistroAdminLocalDTO dto) {
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

    @PutMapping("/usuarios/{dni}/estado")
    public ResponseEntity<Void> cambiarEstadoUsuario(@PathVariable String dni,
                                                     @RequestBody ActualizarEstadoUsuarioDTO dto) {
        adminTotalService.cambiarEstadoUsuarioPorDni(dni, dto.getActivo());
        return ResponseEntity.noContent().build();
    }
}