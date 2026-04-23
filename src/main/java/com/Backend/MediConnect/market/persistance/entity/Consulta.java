package com.Backend.MediConnect.market.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "CONSULTA")
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


}
