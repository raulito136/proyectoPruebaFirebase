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

    private Copia copia;

    private MovieController movieController;

    public void setCopia(Copia copia) {
        this.copia = copia;
        movieTitleLabel.setText(copia.getPelicula().getTitulo());
        formatField.setText(copia.getFormato());
        locationField.setText(copia.getUbicacion());
    }

    public void setMovieController(MovieController movieController) {
        this.movieController = movieController;
    }

    @FXML
    void saveCopy(ActionEvent event) {
        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            em.getTransaction().begin();
            Copia copiaToUpdate = em.find(Copia.class, copia.getId());
            copiaToUpdate.setFormato(formatField.getText());
            copiaToUpdate.setUbicacion(locationField.getText());
            em.getTransaction().commit();

            movieController.refreshCopies();

            Stage stage = (Stage) movieTitleLabel.getScene().getWindow();
            stage.close();
        } finally {
            em.close();
        }
    }
}
