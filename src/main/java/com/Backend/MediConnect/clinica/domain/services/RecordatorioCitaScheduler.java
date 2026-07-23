package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.repository.ICitaRepository;
import com.Backend.MediConnect.clinica.persistance.entity.Cita;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class RecordatorioCitaScheduler {

    private static final Logger log = LoggerFactory.getLogger(RecordatorioCitaScheduler.class);

    private final ICitaRepository citaRepository;
    private final EmailService emailService;

    public RecordatorioCitaScheduler(ICitaRepository citaRepository, EmailService emailService) {
        this.citaRepository = citaRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void enviarRecordatoriosDelDia() {
        LocalDate fechaObjetivo = LocalDate.now().plusDays(1);
        List<Cita> citasConfirmadas = citaRepository.findByFechaCitaAndEstado(fechaObjetivo, "CONFIRMADA");
        List<Cita> citasReprogramadas = citaRepository.findByFechaCitaAndEstado(fechaObjetivo, "REPROGRAMADA");

        procesarLista(citasConfirmadas);
        procesarLista(citasReprogramadas);
    }

    private void procesarLista(List<Cita> citas) {
        for (Cita cita : citas) {
            try {
                emailService.enviarRecordatorioCita(
                        cita.getPaciente().getPersona().getUsuario().getCorreo(),
                        cita.getPaciente().getPersona().getNombres(),
                        cita);
            } catch (Exception e) {
                log.error("Error al enviar recordatorio de la cita {}: {}", cita.getIdCita(), e.getMessage());
            }
        }
    }
}