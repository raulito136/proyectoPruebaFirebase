package com.example.moviecollection.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Copia implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_pelicula")
    private Pelicula pelicula;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private String formato;

    // Cambiamos Soporte por Estado
    private String estado;

    private int cantidad;

    public Copia() {}

    public Copia(Pelicula pelicula, Usuario usuario, String formato, String estado) {
        this.pelicula = pelicula;
        this.usuario = usuario;
        this.formato = formato;
        this.estado = estado;
        this.cantidad = 1;
    }

    // Getters y Setters actualizados
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public long getId() { return id; }
    public Pelicula getPelicula() { return pelicula; }
    public void setPelicula(Pelicula pelicula) { this.pelicula = pelicula; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}