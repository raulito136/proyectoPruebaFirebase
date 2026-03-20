package com.example.moviecollection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class MainController {

    // Componentes de la interfaz (fx:id en main.fxml)
    @FXML private Label welcomeLabel; // [cite: 5]
    @FXML private TableView<Pelicula> movieTable; // [cite: 6]
    @FXML private TableColumn<Pelicula, String> titleColumn; // [cite: 6]
    @FXML private TableColumn<Pelicula, String> directorColumn; // [cite: 6]
    @FXML private TableColumn<Pelicula, Integer> yearColumn; // [cite: 6]
    @FXML private TableColumn<Pelicula, String> genreColumn; // [cite: 7]

    @FXML private TextField titleField; // [cite: 9]
    @FXML private TextField directorField; // [cite: 9]
    @FXML private TextField yearField; // [cite: 9]
    @FXML private TextField genreField; // [cite: 9]

    private Usuario usuario;
    private ObservableList<Pelicula> listaPeliculas = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Vinculación de columnas con los atributos de Pelicula.java
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titulo")); //
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("director")); //
        // MUY IMPORTANTE: Usamos "anoEstreno" porque así se llama en tu clase Pelicula
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("anoEstreno")); //
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genero")); //

        // Listener para cargar datos en los campos al seleccionar una fila
        movieTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                titleField.setText(newSelection.getTitulo());
                directorField.setText(newSelection.getDirector());
                yearField.setText(String.valueOf(newSelection.getAnoEstreno()));
                genreField.setText(newSelection.getGenero());
            }
        });
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {
            welcomeLabel.setText("Bienvenido, " + usuario.getNombre_usuario()); // [cite: 5]
        }
        loadMovies();
    }

    private void loadMovies() {
        EntityManager em = DbManager.getEmf().createEntityManager(); //
        try {
            TypedQuery<Pelicula> query = em.createQuery("SELECT p FROM Pelicula p", Pelicula.class);
            List<Pelicula> resultados = query.getResultList();
            listaPeliculas.setAll(resultados);
            movieTable.setItems(listaPeliculas);
        } finally {
            em.close();
        }
    }

    // --- ACCIONES (onAction en main.fxml) ---

    @FXML
    private void manageMovies() { // Corresponde al botón "Añadir" [cite: 10]
        if (validarCampos()) {
            EntityManager em = DbManager.getEmf().createEntityManager(); //
            try {
                em.getTransaction().begin();
                // Creamos la película con la descripción vacía por ahora (tu constructor la pide)
                Pelicula nueva = new Pelicula(
                        titleField.getText(),
                        genreField.getText(),
                        Integer.parseInt(yearField.getText()),
                        "", // descripción
                        directorField.getText()
                ); //
                em.persist(nueva);
                em.getTransaction().commit();
                loadMovies();
                clearForm();
            } finally {
                em.close();
            }
        }
    }

    @FXML
    private void updateMovie() { // Corresponde al botón "Actualizar" [cite: 10]
        Pelicula seleccionada = movieTable.getSelectionModel().getSelectedItem();
        if (seleccionada != null && validarCampos()) {
            EntityManager em = DbManager.getEmf().createEntityManager();
            try {
                em.getTransaction().begin();
                Pelicula p = em.find(Pelicula.class, seleccionada.getId());
                p.setTitulo(titleField.getText());
                p.setDirector(directorField.getText());
                p.setAnoEstreno(Integer.parseInt(yearField.getText())); //
                p.setGenero(genreField.getText());
                em.getTransaction().commit();
                loadMovies();
            } finally {
                em.close();
            }
        }
    }

    @FXML
    private void deleteMovie() { // Corresponde al botón "Eliminar" [cite: 10]
        Pelicula seleccionada = movieTable.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            EntityManager em = DbManager.getEmf().createEntityManager();
            try {
                em.getTransaction().begin();
                Pelicula p = em.find(Pelicula.class, seleccionada.getId());
                if (p != null) em.remove(p);
                em.getTransaction().commit();
                loadMovies();
                clearForm();
            } finally {
                em.close();
            }
        }
    }

    @FXML
    private void clearForm() {
        titleField.clear();
        directorField.clear();
        yearField.clear();
        genreField.clear();
        movieTable.getSelectionModel().clearSelection();
    }

    private boolean validarCampos() {
        return !titleField.getText().isEmpty() && !yearField.getText().isEmpty();
    }
}