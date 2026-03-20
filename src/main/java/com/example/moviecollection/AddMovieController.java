package com.example.moviecollection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.EntityManager;

public class AddMovieController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField directorField;

    @FXML
    private TextField yearField;

    @FXML
    private TextField genreField;

    @FXML
    private TextArea descriptionArea;

    private AdminController adminController;

    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }

    @FXML
    void saveMovie(ActionEvent event) {
        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            em.getTransaction().begin();
            Pelicula pelicula = new Pelicula(
                    titleField.getText(),
                    genreField.getText(),
                    Integer.parseInt(yearField.getText()),
                    descriptionArea.getText(),
                    directorField.getText()
            );
            em.persist(pelicula);
            em.getTransaction().commit();

            if (adminController != null) {
                adminController.loadMovies();
            }

            Stage stage = (Stage) titleField.getScene().getWindow();
            stage.close();
        } finally {
            em.close();
        }
    }
}
