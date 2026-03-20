package com.example.moviecollection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML
    private ListView<Pelicula> movieListView;

    @FXML
    private Button logoutButton;

    @FXML
    private Button manageMoviesButton;

    @FXML
    private Button closeButton;

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (!usuario.isAdmin()) {
            manageMoviesButton.setVisible(false);
        }
        loadUserMovies();
    }

    private void loadUserMovies() {
        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            TypedQuery<Copia> query = em.createQuery("SELECT c FROM Copia c WHERE c.id_usuario = :id_usuario", Copia.class);
            query.setParameter("id_usuario", usuario.getId());
            List<Copia> copias = query.getResultList();

            ObservableList<Pelicula> peliculas = FXCollections.observableArrayList();
            for (Copia copia : copias) {
                Pelicula pelicula = em.find(Pelicula.class, copia.getId_pelicula());
                peliculas.add(pelicula);
            }
            movieListView.setItems(peliculas);
        } finally {
            em.close();
        }
    }

    @FXML
    private void manageMovies() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/movie.fxml"));
            Stage stage = (Stage) manageMoviesButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root, 300, 275));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void close() {
        System.exit(0);
    }
}
