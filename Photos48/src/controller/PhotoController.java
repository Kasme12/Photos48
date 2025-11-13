package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import model.Photo;

/**
 * Displays full photo and metadata.
 */
public class PhotoController {
    @FXML private ImageView fullImage;
    @FXML private Label captionLabel;
    @FXML private Label dateLabel;
    @FXML private Label tagsLabel;

    private Photo currentPhoto;

    @FXML
    public void initialize() {
        currentPhoto = LoginController.currentUser.getAlbums().get(0).getPhotos().get(0); // example
        fullImage.setImage(new Image("file:" + currentPhoto.getFilePath()));
        captionLabel.setText("Caption: " + currentPhoto.getCaption());
        dateLabel.setText("Date: " + currentPhoto.getDateTaken().toString());
        tagsLabel.setText("Tags: " + currentPhoto.getTags().toString());
    }
}
