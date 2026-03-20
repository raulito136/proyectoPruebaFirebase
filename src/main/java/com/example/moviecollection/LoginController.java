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
            // Si no hay usuarios, crea uno por defecto
            if (em.createQuery("SELECT u FROM User u", User.class).getResultList().isEmpty()) {
                em.getTransaction().begin();
                em.persist(new User("admin", "admin"));
                em.getTransaction().commit();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setHeaderText("Base de datos inicializada");
                alert.setContentText("No se encontraron usuarios. Se ha creado un usuario por defecto 'admin' con contraseña 'admin'.");
                alert.showAndWait();
            }

            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
            query.setParameter("username", usernameField.getText());
            query.setParameter("password", passwordField.getText());
            User usuario = query.getSingleResult();

            openMainWindow(usuario);
        } catch (NoResultException e) {
            errorLabel.setText("Nombre de usuario o contraseña incorrectos.");
        } finally {
            em.close();
        }
    }

    private void openMainWindow(User usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();

            MainController controller = loader.getController();
            controller.setUsuario(usuario);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToRegister(ActionEvent event) {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/register.fxml"));
            stage.setScene(new Scene(root, 300, 275));
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
