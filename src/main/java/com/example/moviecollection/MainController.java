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
import java.util.stream.Collectors;

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
        if (usuario != null && !usuario.isAdmin()) {
            manageMoviesButton.setVisible(false);
        }
        loadUserMovies();
    }

    private void loadUserMovies() {
        if (usuario == null) return;

        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            TypedQuery<Copia> query = em.createQuery("SELECT c FROM Copia c WHERE c.usuario = :usuario", Copia.class);
            query.setParameter("usuario", usuario);
            List<Pelicula> peliculas = query.getResultList().stream()
                    .map(Copia::getPelicula)
                    .distinct()
                    .collect(Collectors.toList());
            ObservableList<Pelicula> observablePeliculas = FXCollections.observableArrayList(peliculas);
            movieListView.setItems(observablePeliculas);
        } finally {
            em.close();
        }
    }

    @FXML
    private void manageMovies() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/movie.fxml"));
            Parent root = loader.load();
            MovieController controller = loader.getController();
            controller.setUsuario(this.usuario);
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
