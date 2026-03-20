package com.example.moviecollection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Copia implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    private long id_pelicula;
    private long id_usuario;
    private String estado;
    private String soporte;
    private String ubicacion;

    public Copia() {
        // Default constructor
    }

    public Copia(long id_pelicula, long id_usuario, String estado, String soporte, String ubicacion) {
        this.id_pelicula = id_pelicula;
        this.id_usuario = id_usuario;
        this.estado = estado;
        this.soporte = soporte;
        this.ubicacion = ubicacion;
    }

    public long getId() {
        return id;
    }

    public long getId_pelicula() {
        return id_pelicula;
    }

    public void setId_pelicula(long id_pelicula) {
        this.id_pelicula = id_pelicula;
    }

    public long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSoporte() {
        return soporte;
    }

    public void setSoporte(String soporte) {
        this.soporte = soporte;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
