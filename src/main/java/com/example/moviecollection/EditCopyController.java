package com.example.moviecollection;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class EditCopyController {

    @FXML
    private TextField formatField;
    @FXML
    private TextField locationField;
    @FXML
    private ComboBox<Usuario> ownerComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private Copia copia;
    private Pelicula pelicula;

    public void initialize() {
        loadUsers();
        ownerComboBox.setConverter(new StringConverter<Usuario>() {
            @Override
            public String toString(Usuario usuario) {
                return usuario == null ? "" : usuario.getNombre_usuario();
            }

            @Override
            public Usuario fromString(String string) {
                return null;
            }
        });
    }

    private void loadUsers() {
        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u", Usuario.class);
            List<Usuario> usuarios = query.getResultList();
            ownerComboBox.setItems(FXCollections.observableArrayList(usuarios));
        } finally {
            em.close();
        }
    }

    public void setCopy(Copia copia, Pelicula pelicula) {
        this.copia = copia;
        this.pelicula = pelicula;

        if (copia != null) {
            formatField.setText(copia.getFormato());
            locationField.setText(copia.getUbicacion());
            EntityManager em = DbManager.getEmf().createEntityManager();
            try {
                ownerComboBox.setValue(em.find(Usuario.class, copia.getId_usuario()));
            } finally {
                em.close();
            }
        }
    }

    @FXML
    private void save() {
        EntityManager em = DbManager.getEmf().createEntityManager();
        em.getTransaction().begin();

        if (copia == null) {
            // Nueva copia
            copia = new Copia();
            copia.setId_pelicula(pelicula.getId());
        }

        copia.setFormato(formatField.getText());
        copia.setUbicacion(locationField.getText());
        copia.setId_usuario(ownerComboBox.getValue().getId());

        em.merge(copia);
        em.getTransaction().commit();
        em.close();

        close();
    }

    @FXML
    private void cancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
