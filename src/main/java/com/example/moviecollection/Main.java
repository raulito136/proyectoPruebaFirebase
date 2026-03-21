package com.example.moviecollection;

import com.example.moviecollection.persistence.DbManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inicializa el EntityManagerFactory al inicio de la aplicación
        DbManager.getEmf();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Movie Collection");

        Scene scene = new Scene(root, 550, 400);
        // Aplicar la hoja de estilos a la escena de login
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        // Cierra el EntityManagerFactory al salir de la aplicación
        DbManager.close();
        super.stop();
    }
}
