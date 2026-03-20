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
    private TableColumn<Copia, String> stateColumn;

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
    private Usuario usuario;

    public void setPeliculaAndUsuario(Pelicula pelicula, Usuario usuario) {
        this.pelicula = pelicula;
        this.usuario = usuario;
        loadCopies();
    }

    private void loadCopies() {
        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            TypedQuery<Copia> query = em.createQuery("SELECT c FROM Copia c WHERE c.pelicula = :pelicula", Copia.class);
            query.setParameter("pelicula", pelicula);
            List<Copia> copias = query.getResultList();
            ObservableList<Copia> observableCopias = FXCollections.observableArrayList(copias);
            copyTableView.setItems(observableCopias);
        } finally {
            em.close();
        }
    }

    public void initialize() {
        formatColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFormato()));

        // Nueva lógica para la columna de estado
        stateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstado()));

        ownerColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUsuario().getNombre_usuario()));
    }

    @FXML
    private void addCopy() {
        openEditCopyDialog(null);
    }

    @FXML
    private void editCopy() {
        Copia selectedCopia = copyTableView.getSelectionModel().getSelectedItem();
        if (selectedCopia != null) {
            // Verifica si el usuario actual es el dueño de la copia
            if (selectedCopia.getUsuario().equals(this.usuario)) {
                openEditCopyDialog(selectedCopia);
            } else {
                showAlert("No es tu copia", "Solo puedes editar tus propias copias.", "Por favor, selecciona una copia que te pertenezca.");
            }
        } else {
            showAlert("Sin selección", "No hay copia seleccionada", "Por favor, selecciona una copia de la tabla.");
        }
    }

    private void openEditCopyDialog(Copia copia) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit_copy.fxml"));
            Parent root = loader.load();

            EditCopyController controller = loader.getController();
            controller.setUsuario(this.usuario);
            controller.setCopia(copia);
            controller.setPelicula(this.pelicula);

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
            if (selectedCopia.getUsuario().equals(this.usuario)) {
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
                showAlert("No es tu copia", "Solo puedes borrar tus propias copias.", "Selecciona una copia que te pertenezca para borrarla.");
            }
        } else {
            showAlert("Sin selección", "No hay copia seleccionada", "Por favor, selecciona una copia de la tabla.");
        }
    }

    @FXML
    private void back() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/movie.fxml"));
            Parent root = loader.load();

            MovieController controller = loader.getController();
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
}