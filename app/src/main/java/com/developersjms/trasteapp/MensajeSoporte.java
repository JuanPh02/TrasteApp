package com.developersjms.trasteapp;

public class MensajeSoporte {
    private int idUsuario;
    private int idTipoProblema;
    private String descripcion;
    private String estado;

    public MensajeSoporte() {
    }

    public MensajeSoporte(int idUsuario, int idTipoProblema, String descripcion, String estado) {
        this.setIdUsuario(idUsuario);
        this.setIdTipoProblema(idTipoProblema);
        this.setDescripcion(descripcion);
        this.setEstado(estado);
    }


    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTipoProblema() {
        return idTipoProblema;
    }

    public void setIdTipoProblema(int idTipoProblema) {
        this.idTipoProblema = idTipoProblema;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
