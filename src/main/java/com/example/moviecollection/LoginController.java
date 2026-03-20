package com.example.moviecollection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button closeButton;

    @FXML
    private Label errorLabel;

    @FXML
    private void login() {
        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            // If no users exist, create a default 'admin' user
            if (em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList().isEmpty()) {
                em.getTransaction().begin();
                em.persist(new Usuario("admin", "admin"));
                em.getTransaction().commit();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setHeaderText("Base de datos inicializada");
                alert.setContentText("No se encontraron usuarios. Se ha creado un usuario por defecto 'admin' con contraseña 'admin'.");
                alert.showAndWait();
            }

            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.nombre_usuario = :nombre_usuario AND u.contrasena = :contrasena", Usuario.class);
            query.setParameter("nombre_usuario", usernameField.getText());
            query.setParameter("contrasena", passwordField.getText());
            Usuario usuario = query.getSingleResult();

            openMainWindow(usuario);
        } catch (NoResultException e) {
            errorLabel.setText("Nombre de usuario o contraseña incorrectos.");
        } finally {
            em.close();
        }
    }

    private void openMainWindow(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();

            MainController controller = loader.getController();
            controller.setUsuario(usuario);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToRegister(ActionEvent event) {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/register.fxml"));
            Scene scene = new Scene(root, 350, 300);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error al cargar la pantalla de registro.");
        }
    }

    @FXML
    private void close() {
        System.exit(0);
    }
}
