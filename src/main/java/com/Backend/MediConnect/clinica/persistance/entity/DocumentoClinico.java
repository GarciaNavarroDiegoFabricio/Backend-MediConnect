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
    @JoinColumn(name = "id_historia", nullable = false)
    private HistoriaClinica historiaClinica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_atencion")
    private AtencionMedica atencionMedica;

    @Column(name = "nombre_archivo", nullable = false, length = 255)
    private String nombreArchivo;

    @Column(name = "url_archivo", nullable = false, length = 500)
    private String urlArchivo;

    @Column(name = "url_archivo_public_id", length = 255)
    private String urlArchivoPublicId;

    @Column(name = "tipo_documento", length = 50)
    private String tipoDocumento;

    @Column(name = "fecha_carga", nullable = false)
    private LocalDateTime fechaCarga;

    @Column(name = "usuario_carga", length = 100)
    private String usuarioCarga;

    @PrePersist
    public void prePersist() {
        this.fechaCarga = LocalDateTime.now();
    }
}