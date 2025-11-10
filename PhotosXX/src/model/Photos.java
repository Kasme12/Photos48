package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main launcher class for the Photo application.
 * Loads data, launches login screen, and saves on exit.
 * @author Esmeralda Bencosme
 */
public class Photos extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        PhotoManager.loadData();  // Load saved users

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Photo App Login");
        primaryStage.show();
    }

    @Override
    public void stop() {
        PhotoManager.saveData();  // Save users on exit
    }

    public static void main(String[] args) {
        launch(args);  // Start JavaFX app
    }
}
