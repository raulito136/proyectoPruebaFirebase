package com.example.moviecollection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML private TableColumn<Copia, String> locationColumn;

    private Usuario usuario;
    private ObservableList<Copia> listaCopias = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        movieTitleColumn.setCellValueFactory(new PropertyValueFactory<>("pelicula"));
        formatColumn.setCellValueFactory(new PropertyValueFactory<>("formato"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));

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
            TypedQuery<Copia> query = em.createQuery("SELECT c FROM Copia c WHERE c.propietario = :usuario", Copia.class);
            query.setParameter("usuario", usuario);
            List<Copia> resultados = query.getResultList();
            listaCopias.setAll(resultados);
            copyTable.setItems(listaCopias);
        } finally {
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
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
