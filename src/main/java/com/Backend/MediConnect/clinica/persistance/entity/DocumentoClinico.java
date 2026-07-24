package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "documento_clinico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Long idDocumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consulta", nullable = false)
    private Consulta consulta;

    @Column(name = "nombre_archivo")
    private String nombreArchivo;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "ruta_archivo")
    private String rutaArchivo;

    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida;

    @PrePersist
    public void prePersist() {
        fechaSubida = LocalDateTime.now();
    }
}