package com.example.moviecollection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    private void login() {
        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            // Si no hay usuarios, crea uno por defecto
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fallo de inicio de sesión");
            alert.setHeaderText("Nombre de usuario o contraseña incorrectos.");
            alert.setContentText("Por favor, comprueba tus credenciales e inténtalo de nuevo.");
            alert.showAndWait();
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
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void close() {
        System.exit(0);
    }
}
