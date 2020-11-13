package com.developersjms.trasteapp;

public class Mudanza {

    private int idMudanza;
    private int idConductor;
    private int idVehiculo;
    private int idCiudadPartida;
    private String direccionPartida;
    private int idCiudadDestino;
    private String direccionDestino;
    private String fechaHora;
    private String descripcion;
    private int estado;
    private int precio;

    public Mudanza(int idMudanza, int idConductor, int idVehiculo, int idCiudadPartida, String direccionPartida, int idCiudadDestino,
                   String direccionDestino, String fechaHora, String descripcion, int estado, int precio) {
        this.idMudanza = idMudanza;
        this.idConductor = idConductor;
        this.idVehiculo = idVehiculo;
        this.idCiudadPartida = idCiudadPartida;
        this.direccionPartida = direccionPartida;
        this.idCiudadDestino = idCiudadDestino;
        this.direccionDestino = direccionDestino;
        this.fechaHora = fechaHora;
        this.descripcion = descripcion;
        this.estado = estado;
        this.precio = precio;
    }

    public int getIdMudanza() {
        return idMudanza;
    }

    public int getIdConductor() {
        return idConductor;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public int getIdCiudadPartida() {
        return idCiudadPartida;
    }

    public String getDireccionPartida() {
        return direccionPartida;
    }

    public int getIdCiudadDestino() {
        return idCiudadDestino;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getEstado() {
        return estado;
    }

    public int getPrecio() {
        return precio;
    }

    public void setIdMudanza(int idMudanza) {
        this.idMudanza = idMudanza;
    }

    public void setIdConductor(int idConductor) {
        this.idConductor = idConductor;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public void setIdCiudadPartida(int idCiudadPartida) {
        this.idCiudadPartida = idCiudadPartida;
    }

    public void setDireccionPartida(String direccionPartida) {
        this.direccionPartida = direccionPartida;
    }

    public void setIdCiudadDestino(int idCiudadDestino) {
        this.idCiudadDestino = idCiudadDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}
