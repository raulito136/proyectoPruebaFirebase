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
    private String ubicacion;

    public Copia() {
        // Default constructor
    }

    public Copia(Pelicula pelicula, Usuario usuario, String formato, String ubicacion) {
        this.pelicula = pelicula;
        this.usuario = usuario;
        this.formato = formato;
        this.ubicacion = ubicacion;
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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
