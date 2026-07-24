package com.Backend.MediConnect.clinica.web.controller;

import com.Backend.MediConnect.clinica.domain.dto.request.AntecedentePacienteRequestDTO;
import com.Backend.MediConnect.clinica.domain.dto.response.ApiResponse;
import com.Backend.MediConnect.clinica.domain.dto.response.AntecedentePacienteResponseDTO;
import com.Backend.MediConnect.clinica.domain.services.AntecedentePacienteService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/antecedentes")
public class AntecedentePacienteController {

        private final AntecedentePacienteService service;

        public AntecedentePacienteController(
                        AntecedentePacienteService service) {

                this.service = service;
        }

        @GetMapping("/paciente/{idPaciente}")
        @PreAuthorize("hasRole('MEDICO')")
        public ResponseEntity<ApiResponse<AntecedentePacienteResponseDTO>> consultar(

                        @PathVariable Long idPaciente) {

                return ResponseEntity.ok(

                                ApiResponse.success(
                                                service.obtener(idPaciente))

                );

        }

        @PutMapping("/paciente/{idPaciente}")
        @PreAuthorize("hasRole('MEDICO')")
        public ResponseEntity<ApiResponse<AntecedentePacienteResponseDTO>> actualizar(

                        @PathVariable Long idPaciente,

                        @Valid @RequestBody AntecedentePacienteRequestDTO request) {

                return ResponseEntity.ok(

                                ApiResponse.success(

                                                "Antecedentes actualizados correctamente.",

                                                service.actualizar(
                                                                idPaciente,
                                                                request,
                                                                "MEDICO")

                                )

                );

        }

}