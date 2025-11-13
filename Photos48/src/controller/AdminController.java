package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import model.User;
import model.UserDataStore;

import java.util.HashMap;

/**
 * Allows admin to create and delete users.
 */
public class AdminController {
    @FXML private TextField newUserField;
    @FXML private ListView<String> userList;

    private HashMap<String, User> users;

    @FXML
    public void initialize() {
        try {
            users = UserDataStore.loadUsers();
            userList.getItems().addAll(users.keySet());
        } catch (Exception e) {
            users = new HashMap<>();
        }
    }

    @FXML
    private void createUser() {
        String username = newUserField.getText().trim();
        if (username.isEmpty() || users.containsKey(username)) {
            showAlert("Invalid or duplicate username.");
            return;
        }
        users.put(username, new User(username));
        userList.getItems().add(username);
        saveUsers();
    }

    @FXML
    private void deleteUser() {
        String selected = userList.getSelectionModel().getSelectedItem();
        if (selected != null && !selected.equals("stock")) {
            users.remove(selected);
            userList.getItems().remove(selected);
            saveUsers();
        }
    }

    private void saveUsers() {
        try {
            UserDataStore.saveUsers(users);
        } catch (Exception e) {
            showAlert("Failed to save users.");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
