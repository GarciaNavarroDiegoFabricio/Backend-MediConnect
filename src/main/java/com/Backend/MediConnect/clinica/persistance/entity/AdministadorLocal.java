package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ADMINISTRADOR_LOCAL")
public class AdministadorLocal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin_local")
    private Integer idAdminLocal;

    @Column(name = "primer_nombre")
    private String primerNombre;

    @Column(name = "segundo_nombre")
    private String segundoNombre;

    @Column(name = "primer_apellido")
    private String primerApellido;

    @Column(name = "segundo_apellido")
    private String segundoApellido;

    @Column(name = "dni", length = 8, nullable = false, unique = true)
    private String dni;

    @ManyToOne
    @JoinColumn(name = "id_sede")
    private Sede sede;

    @OneToMany(mappedBy = "adminLocal")
    private List<Reporte> reportes;

    public AdministadorLocal() {
    };

    public Integer getIdAdminLocal() {
        return idAdminLocal;
    }

    public void setIdAdminLocal(Integer idAdminLocal) {
        this.idAdminLocal = idAdminLocal;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public List<Reporte> getReportes() {
        return reportes;
    }

    public void setReportes(List<Reporte> reportes) {
        this.reportes = reportes;
    }
}
