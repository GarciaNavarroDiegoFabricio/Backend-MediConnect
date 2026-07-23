package com.Backend.MediConnect.clinica.domain.interfaces;

public enum RolUsuario {

    ADMINISTRADOR_TOTAL(1, "Administrador Total",
            "<ul style=\"margin:0; padding-left:18px;\">"
                    + "<li>Gestionar administradores locales de todas las sedes</li>"
                    + "<li>Crear, actualizar, bloquear e inactivar cualquier usuario</li>"
                    + "<li>Supervisar el sistema completo sin restricción por sede</li>"
                    + "</ul>"),

    ADMINISTRADOR_LOCAL(2, "Administrador Local",
            "<ul style=\"margin:0; padding-left:18px;\">"
                    + "<li>Gestionar recepcionistas y médicos de su sede</li>"
                    + "<li>Registrar, actualizar e inactivar usuarios de su sede</li>"
                    + "<li>Consultar reportes y datos administrativos de su sede</li>"
                    + "</ul>"),

    RECEPCIONISTA(3, "Recepcionista",
            "<ul style=\"margin:0; padding-left:18px;\">"
                    + "<li>Registrar pacientes de forma presencial</li>"
                    + "<li>Programar y gestionar citas médicas</li>"
                    + "<li>Consultar el estado de atención de los pacientes</li>"
                    + "</ul>"),

    MEDICO(4, "Médico",
            "<ul style=\"margin:0; padding-left:18px;\">"
                    + "<li>Consultar el historial clínico de tus pacientes asignados</li>"
                    + "<li>Registrar diagnósticos, tratamientos y recetas</li>"
                    + "<li>Gestionar tu agenda de citas médicas</li>"
                    + "</ul>"),

    PACIENTE(5, "Paciente",
            "<ul style=\"margin:0; padding-left:18px;\">"
                    + "<li>Agendar y consultar tus citas médicas</li>"
                    + "<li>Revisar tu historial clínico y recetas</li>"
                    + "<li>Actualizar tus datos de contacto en tu perfil</li>"
                    + "</ul>");

    private final int id;
    private final String nombre;
    private final String descripcionFuncionalidades;

    RolUsuario(int id, String nombre, String descripcionFuncionalidades) {
        this.id = id;
        this.nombre = nombre;
        this.descripcionFuncionalidades = descripcionFuncionalidades;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcionFuncionalidades() {
        return descripcionFuncionalidades;
    }

    public static RolUsuario fromId(int id) {
        for (RolUsuario rol : values()) {
            if (rol.id == id) return rol;
        }
        throw new IllegalArgumentException("Rol no válido: " + id);
    }

    public boolean requiereSede() {
        return this == ADMINISTRADOR_LOCAL || this == RECEPCIONISTA || this == MEDICO;
    }
}