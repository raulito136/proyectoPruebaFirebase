package com.example.moviecollection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.EntityManager;

public class EditCopyController {

    @FXML
    private Label movieTitleLabel;

    @FXML
    private TextField formatField;

    @FXML
    private TextField locationField;

    private Copia copia; // Can be null for a new copy
    private Pelicula pelicula;
    private Usuario usuario;

    public void setCopia(Copia copia) {
        this.copia = copia;
        if (copia != null) {
            formatField.setText(copia.getFormato());
            locationField.setText(copia.getUbicacion());
        }
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
        movieTitleLabel.setText(pelicula.getTitulo());
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    void saveCopy(ActionEvent event) {
        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            em.getTransaction().begin();
            if (this.copia == null) {
                // Create a new copy
                Copia newCopy = new Copia();
                newCopy.setFormato(formatField.getText());
                newCopy.setUbicacion(locationField.getText());
                newCopy.setUsuario(this.usuario);
                newCopy.setPelicula(this.pelicula);
                em.persist(newCopy);
            } else {
                // Update the existing copy
                Copia copiaToUpdate = em.find(Copia.class, this.copia.getId());
                copiaToUpdate.setFormato(formatField.getText());
                copiaToUpdate.setUbicacion(locationField.getText());
                em.merge(copiaToUpdate);
            }
            em.getTransaction().commit();

            Stage stage = (Stage) movieTitleLabel.getScene().getWindow();
            stage.close();
        } finally {
            em.close();
        }
    }

    public void setCopy(Copia selectedCopia, Pelicula pelicula) {
    }
}
