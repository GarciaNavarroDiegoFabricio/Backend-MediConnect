package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "consulta")
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta")
    private Integer idConsulta;

    @ManyToOne
    @JoinColumn(name = "id_medico")
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    private Paciente paciente;

    @OneToMany(mappedBy = "consulta")
    private List<Receta> recetas;

    // Campo nuevo para la finalización
    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;
}
