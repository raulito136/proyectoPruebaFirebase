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

public class MovieController {

    @FXML
    private TableView<Copia> movieTableView;
    @FXML
    private TableColumn<Copia, String> titleColumn;
    @FXML
    private TableColumn<Copia, String> genreColumn;
    @FXML
    private TableColumn<Copia, String> yearColumn;
    @FXML
    private TableColumn<Copia, String> directorColumn;
    @FXML
    private Button addCopyButton;
    @FXML
    private Button editCopyButton;
    @FXML
    private Button deleteCopyButton;
    @FXML
    private Button backButton;

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        loadCopies();
    }

    private void loadCopies() {
        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            TypedQuery<Copia> query = em.createQuery("SELECT c FROM Copia c WHERE c.usuario = :user", Copia.class);
            query.setParameter("user", usuario);
            ObservableList<Copia> observableCopias = FXCollections.observableArrayList(query.getResultList());
            movieTableView.setItems(observableCopias);
        } finally {
            em.close();
        }
    }

    public void initialize() {
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPelicula().getTitulo()));
        genreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPelicula().getGenero()));
        yearColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPelicula().getAno())));
        directorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPelicula().getDirector()));
    }

    @FXML
    private void addCopy() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_copy.fxml"));
            Parent root = loader.load();

            AddCopyController controller = loader.getController();
            controller.setUsuario(usuario);
            controller.setMovieController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Copy");
            stage.setScene(new Scene(root, 400, 350));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editCopy() {
        Copia selectedCopia = movieTableView.getSelectionModel().getSelectedItem();
        if (selectedCopia != null) {
            openEditCopyDialog(selectedCopia);
        } else {
            showAlert("No selection", "No copy selected", "Please select a copy from the table.");
        }
    }

    private void openEditCopyDialog(Copia copia) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit_copy.fxml"));
            Parent root = loader.load();

            EditCopyController controller = loader.getController();
            controller.setCopia(copia);
            controller.setMovieController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Copy");
            stage.setScene(new Scene(root, 400, 250));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteCopy() {
        Copia selectedCopia = movieTableView.getSelectionModel().getSelectedItem();
        if (selectedCopia != null) {
            EntityManager em = DbManager.getEmf().createEntityManager();
            try {
                em.getTransaction().begin();
                em.remove(em.contains(selectedCopia) ? selectedCopia : em.merge(selectedCopia));
                em.getTransaction().commit();
            } finally {
                em.close();
            }
            loadCopies();
        } else {
            showAlert("No selection", "No copy selected", "Please select a copy from the table.");
        }
    }

    @FXML
    private void back() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();
            
            MainController controller = loader.getController();
            controller.setUsuario(this.usuario);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void refreshCopies() {
        loadCopies();
    }
}
