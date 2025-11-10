package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Photos extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Photo App Login");
        primaryStage.show();
    }

    @Override
    public void stop() {
        PhotoManager.saveData();
    }

    public static void main(String[] args) {
        PhotoManager.loadData();
        launch(args);
    }
}
