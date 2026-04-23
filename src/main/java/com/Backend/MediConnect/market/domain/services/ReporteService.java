
package com.Backend.MediConnect.market.domain.services;

import com.Backend.MediConnect.market.domain.dto.ReporteDTO;
import com.Backend.MediConnect.market.persistance.entity.Cita;
import com.Backend.MediConnect.market.domain.repository.CitaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReporteService {

    // Repositorio para acceder a los datos de citas desde la base de datos
    private final CitaRepository citaRepository;

    // Inyección de dependencias mediante constructor
    public ReporteService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    //Genera un reporte de citas basado en una fecha específica.
    public ReporteDTO generarReporteConsulta(LocalDate fecha) {

        List<Cita> citas = citaRepository.findByFecha(fecha);

        int atendidas = 0;
        int canceladas = 0;
        int reprogramadas = 0;
        int pendientes = 0;

        for (Cita cita : citas) {
            // Se convierte el estado a minúsculas para evitar problemas de comparación
            switch (cita.getEstado().toLowerCase()) {
                case "atendida":
                    atendidas++;
                    break;
                case "cancelada":
                    canceladas++;
                    break;
                case "reprogramada":
                    reprogramadas++;
                    break;
                default:
                    pendientes++;
                    break;
            }
        }

        // Retorna el DTO con el resumen del reporte
        return new ReporteDTO(
                fecha,
                atendidas,
                canceladas,
                reprogramadas,
                pendientes
        );
    }
}