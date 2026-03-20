package com.example.moviecollection;

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
    private Soporte soporte;

    private int cantidad;

    public Copia() {
        // Default constructor
    }

    public Copia(Pelicula pelicula, Usuario usuario, String formato, Soporte soporte) {
        this.pelicula = pelicula;
        this.usuario = usuario;
        this.formato = formato;
        this.soporte = soporte;
        this.cantidad = 1;
    }

    public long getId() {
        return id;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public Soporte getSoporte() {
        return soporte;
    }

    public void setSoporte(Soporte soporte) {
        this.soporte = soporte;
    }
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void aumentarCantidad() {
        this.cantidad++;
    }
}
