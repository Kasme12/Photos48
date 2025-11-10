package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import model.PhotoManager;
import model.User;

public class AdminController {
    @FXML
    private ListView<String> userList;
    @FXML
    private TextField newUserField;

    private ObservableList<String> users;

    @FXML
    public void initialize() {
        users = FXCollections.observableArrayList();
        for (User u : PhotoManager.getUsers()) {
            users.add(u.getUsername());
        }
        userList.setItems(users);
    }

    @FXML
    private void handleAddUser() {
        String username = newUserField.getText().trim();
        if (username.isEmpty()) {
            showAlert("Username cannot be empty.");
            return;
        }
        if (PhotoManager.addUser(username)) {
            users.add(username);
            newUserField.clear();
        } else {
            showAlert("User already exists.");
        }
    }

    @FXML
    private void handleDeleteUser() {
        String selected = userList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a user to delete.");
            return;
        }
        if (PhotoManager.deleteUser(selected)) {
            users.remove(selected);
        } else {
            showAlert("Failed to delete user.");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Stage stage = (Stage) userList.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Photo App Login");
        } catch (Exception e) {
            showAlert("Error loading login screen.");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Admin");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
