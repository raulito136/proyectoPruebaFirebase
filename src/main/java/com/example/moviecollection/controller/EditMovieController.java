package com.example.moviecollection.controller;

import com.example.moviecollection.persistence.DbManager;
import com.example.moviecollection.model.Pelicula;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.EntityManager;

public class EditMovieController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField directorField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private Pelicula pelicula;

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;

        if (pelicula != null) {
            titleField.setText(pelicula.getTitulo());
            genreField.setText(pelicula.getGenero());
            yearField.setText(String.valueOf(pelicula.getAnoEstreno()));
            directorField.setText(pelicula.getDirector());
        }
    }

    @FXML
    private void save() {
        EntityManager em = DbManager.getEmf().createEntityManager();
        em.getTransaction().begin();

        if (pelicula == null) {
            pelicula = new Pelicula();
        }

        pelicula.setTitulo(titleField.getText());
        pelicula.setGenero(genreField.getText());
        pelicula.setAnoEstreno(Integer.parseInt(yearField.getText()));
        pelicula.setDirector(directorField.getText());

        em.merge(pelicula);
        em.getTransaction().commit();
        em.close();

        close();
    }

    @FXML
    private void cancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
