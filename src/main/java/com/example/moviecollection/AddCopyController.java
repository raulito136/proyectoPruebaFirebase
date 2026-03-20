package com.example.moviecollection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class AddCopyController {

    @FXML private TableView<Pelicula> movieTable;
    @FXML private TableColumn<Pelicula, String> titleColumn;
    @FXML private TableColumn<Pelicula, String> directorColumn;
    @FXML private TableColumn<Pelicula, Integer> yearColumn;
    @FXML private TableColumn<Pelicula, String> genreColumn;

    private Usuario usuario;
    private ObservableList<Pelicula> listaPeliculas = FXCollections.observableArrayList();

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    private void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("anoEstreno"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genero"));
        loadMovies();
    }

    private void loadMovies() {
        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            TypedQuery<Pelicula> query = em.createQuery("SELECT p FROM Pelicula p", Pelicula.class);
            List<Pelicula> resultados = query.getResultList();
            listaPeliculas.setAll(resultados);
            movieTable.setItems(listaPeliculas);
        } finally {
            em.close();
        }
    }

    @FXML
    void addCopy(ActionEvent event) {
        Pelicula selectedMovie = movieTable.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            EntityManager em = DbManager.getEmf().createEntityManager();
            try {
                em.getTransaction().begin();
                Copia newCopy = new Copia(selectedMovie, usuario, "DVD", "Estantería"); // Ejemplo
                em.persist(newCopy);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
        }
    }
}
