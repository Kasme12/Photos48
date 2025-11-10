package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.PhotoManager;
import model.User;
import controller.UserController;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            showAlert("Username cannot be empty.");
            return;
        }

        if (username.equalsIgnoreCase("admin")) {
            loadScene("/view/admin.fxml", "Admin Dashboard");
        } else {
            User user = PhotoManager.getUser(username);
            if (user == null) {
                showAlert("User not found.");
            } else {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user.fxml"));
                    Scene scene = new Scene(loader.load());
                    UserController controller = loader.getController();
                    controller.setUser(user);
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setTitle("Welcome " + username);
                } catch (Exception e) {
                    showAlert("Failed to load user dashboard.");
                }
            }
        }
    }

    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
        } catch (Exception e) {
            showAlert("Failed to load scene: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
