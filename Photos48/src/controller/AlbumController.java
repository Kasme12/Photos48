package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import model.Album;
import model.Photo;

import java.io.File;

/**
 * Handles photo operations within an album.
 */
public class AlbumController {
    @FXML private ListView<String> photoList;
    @FXML private TextField captionField;
    @FXML private ImageView photoPreview;

    private Album currentAlbum;

    @FXML
    public void initialize() {
        // Load current album from context
        currentAlbum = LoginController.currentUser.getAlbums().get(0); // example
        for (Photo photo : currentAlbum.getPhotos()) {
            photoList.getItems().add(photo.getFilePath());
        }
    }

    @FXML
    private void addPhoto() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg"));
        File file = chooser.showOpenDialog(null);
        if (file != null) {
            Photo photo = new Photo(file.getAbsolutePath());
            currentAlbum.addPhoto(photo);
            photoList.getItems().add(photo.getFilePath());
        }
    }

    @FXML
    private void removePhoto() {
        String selected = photoList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Photo toRemove = new Photo(selected);
            currentAlbum.removePhoto(toRemove);
            photoList.getItems().remove(selected);
        }
    }

    @FXML
    private void updateCaption() {
        String selected = photoList.getSelectionModel().getSelectedItem();
        String caption = captionField.getText().trim();
        if (selected != null && !caption.isEmpty()) {
            for (Photo photo : currentAlbum.getPhotos()) {
                if (photo.getFilePath().equals(selected)) {
                    photo.setCaption(caption);
                    break;
                }
            }
        }
    }
}
