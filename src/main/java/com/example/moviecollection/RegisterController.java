package com.example.moviecollection;

import com.example.moviecollection.model.User;
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
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/database.odb");

    @FXML
    void register(ActionEvent event) {
        EntityManager em = emf.createEntityManager();
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("El nombre de usuario y la contraseña no pueden estar vacíos.");
                return;
            }

            // Check if user already exists
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);

            if (!query.getResultList().isEmpty()) {
                errorLabel.setText("El nombre de usuario ya existe.");
                return;
            }

            // Create and persist the new user
            em.getTransaction().begin();
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password); // In a real app, hash the password!
            em.persist(newUser);
            em.getTransaction().commit();

            // Go back to login screen after successful registration
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
            stage.setScene(new Scene(root, 300, 275));
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error al cargar la pantalla de login.");
        }
    }
}
