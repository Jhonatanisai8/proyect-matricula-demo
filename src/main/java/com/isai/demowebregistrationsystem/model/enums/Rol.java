package com.isai.demowebregistrationsystem.model.enums;

public enum Rol {
    ADMIN("Admin"),
    APODERADO("Apoderado"),
    DOCENTE("Docente"),
    ESTUDIANTE("Estudiante");

    private final String descripcion;

    Rol(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
