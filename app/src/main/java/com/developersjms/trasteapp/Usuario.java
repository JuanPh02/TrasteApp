package com.developersjms.trasteapp;

public class Usuario {
    private String nombres;
    private String apellidos;
    private String fechaNacimiento;
    private String email;
    private String telefono;

    public Usuario() {
    }

    public Usuario(String nombres, String apellidos, String fechaNaciemiento, String telefono, String email) {
        this.setNombres(nombres);
        this.setApellidos(apellidos);
        this.setFechaNacimiento(fechaNaciemiento);
        this.setEmail(email);
        this.setTelefono(telefono);
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
