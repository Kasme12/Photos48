package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import model.User;
import model.UserDataStore;

import java.util.HashMap;

/**
 * Handles login logic for admin and regular users.
 */
public class LoginController {
    @FXML private TextField usernameField;
    @FXML private Button loginButton;

    private static HashMap<String, User> users;

    public static User currentUser;

    @FXML
    public void initialize() {
        try {
            users = UserDataStore.loadUsers();
        } catch (Exception e) {
            users = new HashMap<>();
        }

        // Load stock user if missing
        if (!users.containsKey("stock")) {
            User stockUser = new User("stock");
            // Load stock photos from data/stock/
            // You can add logic here to scan the folder and add to album
            users.put("stock", stockUser);
            try {
                UserDataStore.saveUsers(users);
            } catch (Exception ignored) {}
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            showAlert("Username cannot be empty.");
            return;
        }

        if (username.equals("admin")) {
            loadScene("/view/Admin.fxml");
        } else {
            if (!users.containsKey(username)) {
                users.put(username, new User(username));
            }
            currentUser = users.get(username);
            loadScene("/view/UserHome.fxml");
        }
    }

    private void loadScene(String fxmlPath) {
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource(fxmlPath)));
            stage.setScene(scene);
        } catch (Exception e) {
            showAlert("Failed to load scene.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
