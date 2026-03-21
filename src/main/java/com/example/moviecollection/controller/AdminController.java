package com.example.moviecollection.controller;

import com.example.moviecollection.persistence.DbManager;
import com.example.moviecollection.model.Pelicula;
import com.example.moviecollection.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;

public class AdminController {

    @FXML
    private TableView<Pelicula> movieTable;

    @FXML
    private TableColumn<Pelicula, String> titleColumn;

    @FXML
    private TableColumn<Pelicula, String> directorColumn;

    @FXML
    private TableColumn<Pelicula, Integer> yearColumn;

    private Usuario usuario;

    private ObservableList<Pelicula> movies = FXCollections.observableArrayList();

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("anoEstreno"));

        loadMovies();
    }

    void loadMovies() {
        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            TypedQuery<Pelicula> query = em.createQuery("SELECT p FROM Pelicula p", Pelicula.class);
            movies.setAll(query.getResultList());
            movieTable.setItems(movies);
        } finally {
            em.close();
        }
    }

    @FXML
    void addMovie(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_movie.fxml"));
            Parent root = loader.load();

            AddMovieController controller = loader.getController();
            controller.setAdminController(this);

            Stage stage = new Stage();
            Scene scene = new Scene(root, 500, 400);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteMovie(ActionEvent event) {
        Pelicula selectedMovie = movieTable.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            EntityManager em = DbManager.getEmf().createEntityManager();
            try {
                em.getTransaction().begin();
                Pelicula movieToDelete = em.find(Pelicula.class, selectedMovie.getId());
                em.remove(movieToDelete);
                em.getTransaction().commit();
                loadMovies();
            } finally {
                em.close();
            }
        }
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(root, 500, 400);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
