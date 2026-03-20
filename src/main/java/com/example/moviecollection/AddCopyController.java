package com.example.moviecollection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class AddCopyController {

    @FXML
    private TableView<Pelicula> movieTableView;
    @FXML
    private TableColumn<Pelicula, String> titleColumn;
    @FXML
    private TableColumn<Pelicula, String> directorColumn;
    @FXML
    private TableColumn<Pelicula, Integer> yearColumn;

    private Usuario usuario;
    private MovieController movieController;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setMovieController(MovieController movieController) {
        this.movieController = movieController;
    }

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("ano"));
        loadMovies();
    }

    private void loadMovies() {
        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            TypedQuery<Pelicula> query = em.createQuery("SELECT p FROM Pelicula p", Pelicula.class);
            ObservableList<Pelicula> observablePeliculas = FXCollections.observableArrayList(query.getResultList());
            movieTableView.setItems(observablePeliculas);
        } finally {
            em.close();
        }
    }

    @FXML
    void addCopy(ActionEvent event) {
        Pelicula selectedPelicula = movieTableView.getSelectionModel().getSelectedItem();
        if (selectedPelicula != null) {
            EntityManager em = DbManager.getEmf().createEntityManager();
            try {
                em.getTransaction().begin();
                Copia newCopy = new Copia();
                newCopy.setUsuario(usuario);
                newCopy.setPelicula(selectedPelicula);
                newCopy.setFormato(""); // Set default value
                newCopy.setUbicacion(""); // Set default value
                em.persist(newCopy);
                em.getTransaction().commit();

                movieController.refreshCopies();

                Stage stage = (Stage) movieTableView.getScene().getWindow();
                stage.close();
            } finally {
                em.close();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No selection");
            alert.setHeaderText("No movie selected");
            alert.setContentText("Please select a movie from the table.");
            alert.showAndWait();
        }
    }
}
