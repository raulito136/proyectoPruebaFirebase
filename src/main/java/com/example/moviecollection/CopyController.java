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

public class CopyController {

    @FXML
    private TableView<Copia> copyTableView;
    @FXML
    private TableColumn<Copia, String> formatColumn;
    @FXML
    private TableColumn<Copia, String> locationColumn;
    @FXML
    private TableColumn<Copia, String> ownerColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button backButton;

    private Pelicula pelicula;

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
        loadCopies();
    }

    private void loadCopies() {
        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            TypedQuery<Copia> query = em.createQuery("SELECT c FROM Copia c WHERE c.id_pelicula = :id_pelicula", Copia.class);
            query.setParameter("id_pelicula", pelicula.getId());
            List<Copia> copias = query.getResultList();
            ObservableList<Copia> observableCopias = FXCollections.observableArrayList(copias);
            copyTableView.setItems(observableCopias);
        } finally {
            em.close();
        }
    }

    public void initialize() {
        formatColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSoporte()));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUbicacion()));
        ownerColumn.setCellValueFactory(cellData -> {
            EntityManager em = DbManager.getEmf().createEntityManager();
            try {
                Usuario usuario = em.find(Usuario.class, cellData.getValue().getId_usuario());
                return new SimpleStringProperty(usuario.getNombre_usuario());
            } finally {
                em.close();
            }
        });
    }

    @FXML
    private void addCopy() {
        openEditCopyDialog(null);
    }

    @FXML
    private void editCopy() {
        Copia selectedCopia = copyTableView.getSelectionModel().getSelectedItem();
        if (selectedCopia != null) {
            openEditCopyDialog(selectedCopia);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ninguna selección");
            alert.setHeaderText("No se ha seleccionado ninguna copia");
            alert.setContentText("Por favor, selecciona una copia de la tabla.");
            alert.showAndWait();
        }
    }

    private void openEditCopyDialog(Copia copia) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit_copy.fxml"));
            Parent root = loader.load();

            EditCopyController controller = loader.getController();
            controller.setCopy(copia, pelicula);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(copia == null ? "Añadir Copia" : "Editar Copia");
            stage.setScene(new Scene(root, 400, 250));
            stage.showAndWait();

            loadCopies();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteCopy() {
        Copia selectedCopia = copyTableView.getSelectionModel().getSelectedItem();
        if (selectedCopia != null) {
            EntityManager em = DbManager.getEmf().createEntityManager();
            em.getTransaction().begin();
            em.remove(em.contains(selectedCopia) ? selectedCopia : em.merge(selectedCopia));
            em.getTransaction().commit();
            em.close();

            loadCopies();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ninguna selección");
            alert.setHeaderText("No se ha seleccionado ninguna copia");
            alert.setContentText("Por favor, selecciona una copia de la tabla.");
            alert.showAndWait();
        }
    }

    @FXML
    private void back() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/movie.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
