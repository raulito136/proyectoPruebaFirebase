package com.example.moviecollection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Pelicula implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    private String titulo;
    private String genero;
    private int anio;
    private String descripcion;
    private String director;

    public Pelicula(String titulo, String genero, int anio, String descripcion, String director) {
        this.titulo = titulo;
        this.genero = genero;
        this.anio = anio;
        this.descripcion = descripcion;
        this.director = director;
    }

    public long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
