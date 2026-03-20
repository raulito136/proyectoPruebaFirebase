package com.example.moviecollection.controller;

import com.example.moviecollection.model.Copia;
import com.example.moviecollection.persistence.DbManager;
import com.example.moviecollection.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML private Label welcomeLabel;
    @FXML private TableView<Copia> copyTable;
    @FXML private TableColumn<Copia, String> movieTitleColumn;
    @FXML private TableColumn<Copia, String> formatColumn;
    @FXML private TableColumn<Copia, String> supportColumn;
    @FXML private TableColumn<Copia, Integer> quantityColumn;
    @FXML private MenuBar menuBar;

    private Usuario usuario;
    private ObservableList<Copia> listaCopias = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Título (vía Película)
        movieTitleColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPelicula().getTitulo())
        );

        // Formato
        formatColumn.setCellValueFactory(new PropertyValueFactory<>("formato"));

        // NUEVO: Conectar la columna de soporte con el atributo 'estado' de la clase Copia
        supportColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // NUEVO: Conectar la columna de cantidad con el atributo 'cantidad' de la clase Copia
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {
            welcomeLabel.setText("Bienvenido, " + usuario.getNombre_usuario());
            loadCopies();
        }
    }

    private void loadCopies() {
        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            // 1. IMPORTANTE: 'merge' devuelve una NUEVA instancia gestionada.
            // Debemos usar esa instancia en la consulta.
            Usuario usuarioGestionado = em.merge(this.usuario);

            // 2. Asegúrate de que la consulta use el nombre de atributo correcto 'usuario'
            TypedQuery<Copia> query = em.createQuery(
                    "SELECT c FROM Copia c WHERE c.usuario = :user", Copia.class);

            // 3. Pasamos el usuario gestionado (el que devolvió el merge)
            query.setParameter("user", usuarioGestionado);

            List<Copia> resultados = query.getResultList();

            // 4. Actualizar la lista en el hilo de JavaFX
            listaCopias.setAll(resultados);
            copyTable.setItems(listaCopias);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5. Siempre cerrar el EntityManager
            em.close();
        }
    }

    @FXML
    private void addCopy() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_copy.fxml"));
            Parent root = loader.load();

            AddCopyController controller = loader.getController();
            controller.setUsuario(usuario);

            Stage stage = new Stage();
            stage.setTitle("Añadir Copia");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadCopies();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout() {
        try {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editCopy() {
        Copia selectedCopia = copyTable.getSelectionModel().getSelectedItem();
        if (selectedCopia != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit_copy.fxml"));
                Parent root = loader.load();

                EditCopyController controller = loader.getController();
                controller.setUsuario(this.usuario); //
                controller.setPelicula(selectedCopia.getPelicula()); //
                controller.setCopia(selectedCopia); //

                Stage stage = new Stage();
                stage.setTitle("Editar Copia");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.showAndWait();

                loadCopies(); // Refrescar tabla tras editar
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Atención", "Selecciona una copia para editar.");
        }
    }

    @FXML
    private void deleteCopy() {
        Copia selectedCopia = copyTable.getSelectionModel().getSelectedItem();
        if (selectedCopia != null) {
            EntityManager em = DbManager.getEmf().createEntityManager();
            try {
                em.getTransaction().begin();
                // Buscamos la instancia gestionada
                Copia copiaEnBase = em.find(Copia.class, selectedCopia.getId());

                if (copiaEnBase.getCantidad() > 1) {
                    // Si hay varias, restamos una unidad
                    copiaEnBase.setCantidad(copiaEnBase.getCantidad() - 1);
                    em.merge(copiaEnBase);
                } else {
                    // Si solo queda una, eliminamos el registro
                    em.remove(copiaEnBase);
                }

                em.getTransaction().commit();
                loadCopies(); // Refrescar la tabla
            } finally {
                em.close();
            }
        } else {
            mostrarAlerta("Atención", "Selecciona una copia para eliminar.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
