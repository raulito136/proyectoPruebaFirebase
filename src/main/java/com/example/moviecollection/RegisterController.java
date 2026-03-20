package com.example.moviecollection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import javafx.scene.control.CheckBox;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private CheckBox adminCheckBox;

    // Se ha eliminado la línea que creaba una base de datos diferente ($objectdb/db/database.odb)

    @FXML
    void register(ActionEvent event) {
        // Ahora usamos el DbManager centralizado para conectar a la base de datos correcta
        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();
            boolean isAdmin = adminCheckBox.isSelected();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("El nombre de usuario y la contraseña no pueden estar vacíos.");
                return;
            }

            // Verificar si el usuario ya existe en la base de datos unificada
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.nombre_usuario = :username", Usuario.class);
            query.setParameter("username", username);

            if (!query.getResultList().isEmpty()) {
                errorLabel.setText("El nombre de usuario ya existe.");
                return;
            }

            // Crear y persistir el nuevo usuario
            em.getTransaction().begin();
            Usuario newUser = new Usuario(username, password, isAdmin);
            em.persist(newUser);
            em.getTransaction().commit();

            // Volver a la pantalla de login tras el éxito
            goToLogin(event);

        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    @FXML
    void goToLogin(ActionEvent event) {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(root, 300, 275);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error al cargar la pantalla de login.");
        }
    }
}