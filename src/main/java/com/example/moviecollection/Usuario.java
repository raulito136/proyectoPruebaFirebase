package com.example.moviecollection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Usuario implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    private String nombre_usuario;
    private String contrasena;
    private boolean admin;

    public Usuario(String nombre_usuario, String contrasena, boolean admin) {
        this.nombre_usuario = nombre_usuario;
        this.contrasena = contrasena;
        this.admin = admin;
    }
    public Usuario() {
        // Default constructor
    }


    public long getId() {
        return id;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
