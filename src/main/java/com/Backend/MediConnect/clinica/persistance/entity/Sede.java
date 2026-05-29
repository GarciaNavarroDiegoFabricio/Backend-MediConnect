package com.Backend.MediConnect.clinica.persistance.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "SEDE")
public class Sede {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sede")
    private Integer idSede;

    @Column(name = "nombre_sede")
    private String nombreSede;

    private String ubicacion;

    @ManyToMany(mappedBy = "sedes")
    private List<Medico> medicos;

    @OneToMany(mappedBy = "sede")
    private List<Cita> citas;

    @OneToMany(mappedBy = "sede")
    private List<AdministadorLocal> adminsLocales;

    public Sede() {
    };

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public void setMedicos(List<Medico> medicos) {
        this.medicos = medicos;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public List<AdministadorLocal> getAdminsLocales() {
        return adminsLocales;
    }

    public void setAdminsLocales(List<AdministadorLocal> adminsLocales) {
        this.adminsLocales = adminsLocales;
    }
}
