package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import model.Album;
import model.Photo;
import model.User;

public class AlbumController {
    @FXML
    private ListView<String> photoList;
    @FXML
    private TextField photoPathField;

    private ObservableList<String> photos;
    private Album currentAlbum;
    private User currentUser;

    public void setAlbum(User user, Album album) {
        this.currentUser = user;
        this.currentAlbum = album;
        photos = FXCollections.observableArrayList();
        for (Photo p : album.getPhotos()) {
            photos.add(p.getFilePath());
        }
        photoList.setItems(photos);
    }

    @FXML
    private void handleAddPhoto() {
        String path = photoPathField.getText().trim();
        if (path.isEmpty()) {
            showAlert("Photo path cannot be empty.");
            return;
        }
        Photo photo = new Photo(path);
        if (currentAlbum.addPhoto(photo)) {
            photos.add(photo.getFilePath());
            photoPathField.clear();
        } else {
            showAlert("Photo already exists in album.");
        }
    }

    @FXML
    private void handleRemovePhoto() {
        String selected = photoList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a photo to remove.");
            return;
        }
        Photo toRemove = null;
        for (Photo p : currentAlbum.getPhotos()) {
            if (p.getFilePath().equals(selected)) {
                toRemove = p;
                break;
            }
        }
        if (toRemove != null && currentAlbum.removePhoto(toRemove)) {
            photos.remove(selected);
        } else {
            showAlert("Failed to remove photo.");
        }
    }

    @FXML
    private void handleOpenPhoto() {
        String selected = photoList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a photo to open.");
            return;
        }
        Photo photo = null;
        for (Photo p : currentAlbum.getPhotos()) {
            if (p.getFilePath().equals(selected)) {
                photo = p;
                break;
            }
        }
        if (photo != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photo.fxml"));
                Scene scene = new Scene(loader.load());
                PhotoController controller = loader.getController();
                controller.setPhoto(currentUser, currentAlbum, photo);
                Stage stage = (Stage) photoList.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Photo Viewer");
            } catch (Exception e) {
                showAlert("Error loading photo view.");
            }
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user.fxml"));
            Scene scene = new Scene(loader.load());
            UserController controller = loader.getController();
            controller.setUser(currentUser);
            Stage stage = (Stage) photoList.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Welcome " + currentUser.getUsername());
        } catch (Exception e) {
            showAlert("Error returning to user dashboard.");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Album");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
