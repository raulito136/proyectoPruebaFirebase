package com.example.moviecollection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class AddCopyController {

    @FXML private TableView<Pelicula> movieTable;
    @FXML private TableColumn<Pelicula, String> titleColumn;
    @FXML private TableColumn<Pelicula, String> directorColumn;
    @FXML private TableColumn<Pelicula, Integer> yearColumn;
    @FXML private TableColumn<Pelicula, String> genreColumn;
    @FXML private ComboBox<String> formatComboBox;

    // Cambiado de supportComboBox a stateComboBox para manejar el Estado
    @FXML private ComboBox<String> stateComboBox;

    private Usuario usuario;
    private ObservableList<Pelicula> listaPeliculas = FXCollections.observableArrayList();

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    private void initialize() {
        // Configuración de las columnas de la tabla de películas
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("anoEstreno"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genero"));

        // Opciones para los ComboBox
        formatComboBox.setItems(FXCollections.observableArrayList("DVD", "Blu-ray", "Digital", "VHS"));

        // Solo permite los estados que solicitaste
        stateComboBox.setItems(FXCollections.observableArrayList("Nuevo", "Usado", "Bueno", "Dañado"));

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
        String selectedFormat = formatComboBox.getSelectionModel().getSelectedItem();
        String selectedState = stateComboBox.getSelectionModel().getSelectedItem();

        if (selectedMovie != null && selectedFormat != null && selectedState != null) {
            EntityManager em = DbManager.getEmf().createEntityManager();
            try {
                em.getTransaction().begin();

                // Unimos las entidades al gestor actual
                Pelicula movieManaged = em.merge(selectedMovie);
                Usuario userManaged = em.merge(this.usuario);

                // --- LÓGICA PARA EVITAR DUPLICADOS ---
                // Buscamos si ya existe una copia idéntica para este usuario
                TypedQuery<Copia> query = em.createQuery(
                        "SELECT c FROM Copia c WHERE c.pelicula = :p AND c.usuario = :u " +
                                "AND c.formato = :f AND c.estado = :e", Copia.class);

                query.setParameter("p", movieManaged);
                query.setParameter("u", userManaged);
                query.setParameter("f", selectedFormat);
                query.setParameter("e", selectedState);

                List<Copia> listaExistente = query.getResultList();

                if (!listaExistente.isEmpty()) {
                    // Si existe, recuperamos la copia y aumentamos su cantidad
                    Copia copiaExistente = listaExistente.get(0);
                    copiaExistente.setCantidad(copiaExistente.getCantidad() + 1);
                    em.merge(copiaExistente);
                } else {
                    // Si no existe, creamos una nueva (el constructor ya pone cantidad = 1)
                    Copia newCopy = new Copia(movieManaged, userManaged, selectedFormat, selectedState);
                    em.persist(newCopy);
                }
                // ---------------------------------------

                em.getTransaction().commit();

                Stage stage = (Stage) movieTable.getScene().getWindow();
                stage.close();

            } catch (Exception e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                e.printStackTrace();
            } finally {
                em.close();
            }
        }
    }
}