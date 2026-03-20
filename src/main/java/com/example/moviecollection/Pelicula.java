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
    private int anoEstreno;
    private String descripcion;
    private String director;

    // No-argument constructor required by JPA
    public Pelicula() {}

    public Pelicula(String titulo, String genero, int anoEstreno, String descripcion, String director) {
        this.titulo = titulo;
        this.genero = genero;
        this.anoEstreno = anoEstreno;
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

    public int getAnoEstreno() {
        return anoEstreno;
    }

    public void setAnoEstreno(int anoEstreno) {
        this.anoEstreno = anoEstreno;
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
