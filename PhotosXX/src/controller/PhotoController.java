package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import model.Photo;
import model.Tag;
import model.Album;
import model.User;

import java.io.File;

public class PhotoController {
    @FXML
    private ImageView photoView;
    @FXML
    private TextField captionField;
    @FXML
    private Label dateLabel;
    @FXML
    private ListView<String> tagList;
    @FXML
    private TextField tagNameField;
    @FXML
    private TextField tagValueField;

    private ObservableList<String> tags;
    private Photo currentPhoto;
    private Album currentAlbum;
    private User currentUser;

    public void setPhoto(User user, Album album, Photo photo) {
        this.currentUser = user;
        this.currentAlbum = album;
        this.currentPhoto = photo;

        File file = new File(photo.getFilePath());
        if (file.exists()) {
            photoView.setImage(new Image(file.toURI().toString()));
        }

        captionField.setText(photo.getCaption());
        dateLabel.setText(photo.getDateTaken().toString());

        tags = FXCollections.observableArrayList();
        for (Tag t : photo.getTags()) {
            tags.add(t.toString());
        }
        tagList.setItems(tags);
    }

    @FXML
    private void handleUpdateCaption() {
        String caption = captionField.getText().trim();
        currentPhoto.setCaption(caption);
        showAlert("Caption updated.");
    }

    @FXML
    private void handleAddTag() {
        String name = tagNameField.getText().trim();
        String value = tagValueField.getText().trim();
        if (name.isEmpty() || value.isEmpty()) {
            showAlert("Tag name and value cannot be empty.");
            return;
        }
        Tag tag = new Tag(name, value);
        if (!currentPhoto.getTags().contains(tag)) {
            currentPhoto.addTag(tag);
            tags.add(tag.toString());
            tagNameField.clear();
            tagValueField.clear();
        } else {
            showAlert("Tag already exists.");
        }
    }

    @FXML
    private void handleRemoveTag() {
        String selected = tagList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a tag to remove.");
            return;
        }
        Tag toRemove = null;
        for (Tag t : currentPhoto.getTags()) {
            if (t.toString().equals(selected)) {
                toRemove = t;
                break;
            }
        }
        if (toRemove != null) {
            currentPhoto.removeTag(toRemove);
            tags.remove(selected);
        } else {
            showAlert("Failed to remove tag.");
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/album.fxml"));
            Scene scene = new Scene(loader.load());
            AlbumController controller = loader.getController();
            controller.setAlbum(currentUser, currentAlbum);
            Stage stage = (Stage) photoView.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Album: " + currentAlbum.getName());
        } catch (Exception e) {
            showAlert("Error returning to album view.");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Photo");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
