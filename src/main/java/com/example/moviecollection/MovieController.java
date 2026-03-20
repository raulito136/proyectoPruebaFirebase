package com.example.moviecollection;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.List;

public class MovieController {

    @FXML
    private TableView<Pelicula> movieTableView;
    @FXML
    private TableColumn<Pelicula, String> titleColumn;
    @FXML
    private TableColumn<Pelicula, String> genreColumn;
    @FXML
    private TableColumn<Pelicula, String> yearColumn;
    @FXML
    private TableColumn<Pelicula, String> directorColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button manageCopiesButton;
    @FXML
    private Button backButton;

    private void loadMovies() {
        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            TypedQuery<Pelicula> query = em.createQuery("SELECT p FROM Pelicula p", Pelicula.class);
            List<Pelicula> peliculas = query.getResultList();
            ObservableList<Pelicula> observablePeliculas = FXCollections.observableArrayList(peliculas);
            movieTableView.setItems(observablePeliculas);
        } finally {
            em.close();
        }
    }

    public void initialize() {
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitulo()));
        genreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenero()));
        yearColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAno())));
        directorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDirector()));

        loadMovies();
    }

    @FXML
    private void addMovie() {
        openEditMovieDialog(null);
    }

    @FXML
    private void editMovie() {
        Pelicula selectedPelicula = movieTableView.getSelectionModel().getSelectedItem();
        if (selectedPelicula != null) {
            openEditMovieDialog(selectedPelicula);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ninguna selección");
            alert.setHeaderText("No se ha seleccionado ninguna película");
            alert.setContentText("Por favor, selecciona una película de la tabla.");
            alert.showAndWait();
        }
    }

    private void openEditMovieDialog(Pelicula pelicula) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit_movie.fxml"));
            Parent root = loader.load();

            EditMovieController controller = loader.getController();
            controller.setPelicula(pelicula);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(pelicula == null ? "Añadir Película" : "Editar Película");
            stage.setScene(new Scene(root, 400, 250));
            stage.showAndWait();

            loadMovies();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteMovie() {
        Pelicula selectedPelicula = movieTableView.getSelectionModel().getSelectedItem();
        if (selectedPelicula != null) {
            EntityManager em = DbManager.getEmf().createEntityManager();
            em.getTransaction().begin();
            em.remove(em.contains(selectedPelicula) ? selectedPelicula : em.merge(selectedPelicula));
            em.getTransaction().commit();
            em.close();

            loadMovies();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ninguna selección");
            alert.setHeaderText("No se ha seleccionado ninguna película");
            alert.setContentText("Por favor, selecciona una película de la tabla.");
            alert.showAndWait();
        }
    }

    @FXML
    private void manageCopies() {
        Pelicula selectedPelicula = movieTableView.getSelectionModel().getSelectedItem();
        if (selectedPelicula != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/copy.fxml"));
                Parent root = loader.load();

                CopyController controller = loader.getController();
                controller.setPelicula(selectedPelicula);

                Stage stage = (Stage) manageCopiesButton.getScene().getWindow();
                stage.setScene(new Scene(root, 600, 400));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ninguna selección");
            alert.setHeaderText("No se ha seleccionado ninguna película");
            alert.setContentText("Por favor, selecciona una película de la tabla.");
            alert.showAndWait();
        }
    }

    @FXML
    private void back() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
