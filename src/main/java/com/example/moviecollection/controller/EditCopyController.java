package com.example.moviecollection.controller;

import com.example.moviecollection.model.Copia;
import com.example.moviecollection.persistence.DbManager;
import com.example.moviecollection.model.Pelicula;
import com.example.moviecollection.model.Usuario;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class EditCopyController {

    @FXML
    private Label movieTitleLabel;

    @FXML
    private ComboBox<String> formatComboBox; // Corregido: Coincide con el fx:id del FXML

    @FXML
    private ComboBox<String> stateComboBox;

    private Copia copia;
    private Pelicula pelicula;
    private Usuario usuario;

    @FXML
    public void initialize() {
        // Configuramos las opciones de los desplegables
        formatComboBox.setItems(FXCollections.observableArrayList("DVD", "Blu-ray", "Digital", "VHS"));
        stateComboBox.setItems(FXCollections.observableArrayList("Nuevo", "Usado", "Bueno", "Dañado"));
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
        if (pelicula != null) {
            movieTitleLabel.setText(pelicula.getTitulo());
        }
    }

    public void setCopia(Copia copia) {
        this.copia = copia;
        if (copia != null) {
            // Corregido: Usamos formatComboBox y setValue
            formatComboBox.setValue(copia.getFormato());
            stateComboBox.setValue(copia.getEstado());
        }
    }

    @FXML
    void saveCopy(ActionEvent event) {
        EntityManager em = DbManager.getEmf().createEntityManager();
        try {
            em.getTransaction().begin();

            String nuevoFormato = formatComboBox.getValue();
            String nuevoEstado = stateComboBox.getValue();

            // Usamos <> en lugar de != para evitar errores de sintaxis en JPQL
            TypedQuery<Copia> query = em.createQuery(
                    "SELECT c FROM Copia c WHERE c.pelicula = :p AND c.usuario = :u " +
                            "AND c.formato = :f AND c.estado = :e AND c.id <> :idActual", Copia.class);

            query.setParameter("p", em.merge(this.pelicula));
            query.setParameter("u", em.merge(this.usuario));
            query.setParameter("f", nuevoFormato);
            query.setParameter("e", nuevoEstado);
            query.setParameter("idActual", this.copia.getId());

            List<Copia> duplicados = query.getResultList();

            if (!duplicados.isEmpty()) {
                Copia copiaExistente = duplicados.get(0);
                Copia copiaSiendoEditada = em.find(Copia.class, this.copia.getId());

                copiaExistente.setCantidad(copiaExistente.getCantidad() + copiaSiendoEditada.getCantidad());
                em.remove(copiaSiendoEditada);
                em.merge(copiaExistente);
            } else {
                Copia copiaToUpdate = em.find(Copia.class, this.copia.getId());
                copiaToUpdate.setFormato(nuevoFormato);
                copiaToUpdate.setEstado(nuevoEstado);
                em.merge(copiaToUpdate);
            }

            em.getTransaction().commit();
            closeWindow();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) movieTitleLabel.getScene().getWindow();
        stage.close();
    }
}