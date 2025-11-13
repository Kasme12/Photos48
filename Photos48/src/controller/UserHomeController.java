package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import model.User;
import model.Album;

/**
 * Manages user's album list and album operations.
 */
public class UserHomeController {
    @FXML private ListView<String> albumList;
    @FXML private TextField albumNameField;

    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = LoginController.currentUser;
        for (Album album : currentUser.getAlbums()) {
            albumList.getItems().add(album.getName());
        }
    }

    @FXML
    private void createAlbum() {
        String name = albumNameField.getText().trim();
        if (name.isEmpty() || currentUser.getAlbum(name) != null) {
            showAlert("Invalid or duplicate album name.");
            return;
        }
        currentUser.addAlbum(name);
        albumList.getItems().add(name);
    }

    @FXML
    private void deleteAlbum() {
        String selected = albumList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            currentUser.deleteAlbum(selected);
            albumList.getItems().remove(selected);
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
