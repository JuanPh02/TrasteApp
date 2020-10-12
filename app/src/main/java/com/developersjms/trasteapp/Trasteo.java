package com.developersjms.trasteapp;

public class Trasteo {

    private int idUsuario;
    private int idCiudadPartida;
    private String direccionPartida;
    private int idCiudadDestino;
    private String direccionDestino;
    private String fechaHora;
    private int idCapacidadVehiculo;
    private String descripcionObjetos;
    private String estado;

    public Trasteo() {
    }

    public Trasteo(int idUsuario, int idCiudadPartida, String direccionPartida, int idCiudadDestino, String direccionDestino,
                   String fechaHora, int idCapacidadVehiculo, String descripcionObjetos, String estado) {
        this.setIdUsuario(idUsuario);
        this.setIdCiudadPartida(idCiudadPartida);
        this.setDireccionPartida(direccionPartida);
        this.setIdCiudadDestino(idCiudadDestino);
        this.setDireccionDestino(direccionDestino);
        this.setFechaHora(fechaHora);
        this.setIdCapacidadVehiculo(idCapacidadVehiculo);
        this.setDescripcionObjetos(descripcionObjetos);
        this.setEstado(estado);
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdCiudadPartida() {
        return idCiudadPartida;
    }

    public void setIdCiudadPartida(int idCiudadPartida) {
        this.idCiudadPartida = idCiudadPartida;
    }

    public String getDireccionPartida() {
        return direccionPartida;
    }

    public void setDireccionPartida(String direccionPartida) {
        this.direccionPartida = direccionPartida;
    }

    public int getIdCiudadDestino() {
        return idCiudadDestino;
    }

    public void setIdCiudadDestino(int idCiudadDestino) {
        this.idCiudadDestino = idCiudadDestino;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getIdCapacidadVehiculo() {
        return idCapacidadVehiculo;
    }

    public void setIdCapacidadVehiculo(int idCapacidadVehiculo) {
        this.idCapacidadVehiculo = idCapacidadVehiculo;
    }

    public String getDescripcionObjetos() {
        return descripcionObjetos;
    }

    public void setDescripcionObjetos(String descripcionObjetos) {
        this.descripcionObjetos = descripcionObjetos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
