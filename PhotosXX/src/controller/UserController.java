package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import model.Album;
import model.PhotoManager;
import model.User;

public class UserController {
    @FXML
    private ListView<String> albumList;
    @FXML
    private TextField albumNameField;

    private ObservableList<String> albums;
    private User currentUser;

    @FXML
    public void initialize() {
        // Will be set by LoginController
    }

    public void setUser(User user) {
        this.currentUser = user;
        albums = FXCollections.observableArrayList();
        for (Album a : user.getAlbums()) {
            albums.add(a.getName());
        }
        albumList.setItems(albums);
    }

    @FXML
    private void handleCreateAlbum() {
        String name = albumNameField.getText().trim();
        if (name.isEmpty()) {
            showAlert("Album name cannot be empty.");
            return;
        }
        if (currentUser.addAlbum(name)) {
            albums.add(name);
            albumNameField.clear();
        } else {
            showAlert("Album already exists.");
        }
    }

    @FXML
    private void handleDeleteAlbum() {
        String selected = albumList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select an album to delete.");
            return;
        }
        if (currentUser.deleteAlbum(selected)) {
            albums.remove(selected);
        } else {
            showAlert("Failed to delete album.");
        }
    }

    @FXML
    private void handleRenameAlbum() {
        String selected = albumList.getSelectionModel().getSelectedItem();
        String newName = albumNameField.getText().trim();
        if (selected == null || newName.isEmpty()) {
            showAlert("Select an album and enter a new name.");
            return;
        }
        Album album = currentUser.getAlbum(selected);
        if (album == null || currentUser.getAlbum(newName) != null) {
            showAlert("Invalid rename.");
            return;
        }
        album.setName(newName);
        albums.set(albums.indexOf(selected), newName);
        albumNameField.clear();
    }

    @FXML
    private void handleOpenAlbum() {
        String selected = albumList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select an album to open.");
            return;
        }
        Album album = currentUser.getAlbum(selected);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/album.fxml"));
            Scene scene = new Scene(loader.load());
            AlbumController controller = loader.getController();
            controller.setAlbum(currentUser, album);
            Stage stage = (Stage) albumList.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Album: " + selected);
        } catch (Exception e) {
            showAlert("Error loading album view.");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Stage stage = (Stage) albumList.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Photo App Login");
        } catch (Exception e) {
            showAlert("Error loading login screen.");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
