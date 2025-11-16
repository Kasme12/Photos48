package photos48.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import photos48.model.Photo;
import photos48.model.Tag;
import photos48.model.User;
import photos48.service.PhotoService;
import photos48.service.TagService;
import photos48.ui.Photos;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
 

/**
 * Controller for Photo Viewer (image display, caption, tags, add/delete tags).
 * @author Esmeralda Bencosme
 */
public class PhotoViewerController {

    @FXML private ImageView photoImageView;
    @FXML private TextArea captionTextArea;
    @FXML private Label dateTimeLabel;
    @FXML private ListView<String> tagsListView;
    @FXML private ComboBox<String> tagTypeCombo;
    @FXML private TextField tagValueField;
    @FXML private Button backBtn;
    @FXML private ScrollPane imageScrollPane;
    @FXML private Slider zoomSlider;
    @FXML private Button zoomInBtn, zoomOutBtn, resetZoomBtn;

    private final DoubleProperty zoomFactor = new SimpleDoubleProperty(1.0);

    private Photo currentPhoto;
    private TagService tagService;
    private PhotoService photoService;

    public void setPhoto(Photo photo, TagService ts) {
        this.currentPhoto = photo;
        this.tagService = ts;
        this.photoService = new PhotoService(ts.getUser());
        
        // Load and display image
        try {
            File imageFile = new File(photo.getPath());
            if (imageFile.exists()) {
                try (InputStream is = new FileInputStream(imageFile)) {
                    Image img = new Image(is);
                    if (img.isError()) throw new Exception("Failed to load image");
                    photoImageView.setImage(img);
                    photoImageView.setPreserveRatio(true);
                    
                    // Fit image to scroll pane dimensions, maintaining aspect ratio
                    // Bind both width and height to ensure complete image is visible
                    photoImageView.fitWidthProperty().bind(
                        imageScrollPane.viewportBoundsProperty().map(bounds -> 
                            bounds.getWidth() * zoomFactor.get()
                        )
                    );
                    photoImageView.fitHeightProperty().bind(
                        imageScrollPane.viewportBoundsProperty().map(bounds -> 
                            bounds.getHeight() * zoomFactor.get()
                        )
                    );
                    
                    // Update image size when zoom changes
                    zoomFactor.addListener((obs, oldV, newV) -> {
                        photoImageView.setFitWidth(imageScrollPane.getViewportBounds().getWidth() * newV.doubleValue());
                        photoImageView.setFitHeight(imageScrollPane.getViewportBounds().getHeight() * newV.doubleValue());
                    });
                    
                    // Initialize zoom controls
                    zoomSlider.valueProperty().addListener((obs, oldV, newV) -> {
                        zoomFactor.set(newV.doubleValue());
                    });
                    
                    // Set initial zoom to show complete image
                    zoomSlider.setValue(1.0);
                    zoomFactor.set(1.0);
                }
            } else {
                showError("Image file not found: " + photo.getPath());
            }
        } catch (Exception e) {
            showError("Failed to load image: " + e.getMessage());
        }
        
        captionTextArea.setText(photo.getCaption() != null ? photo.getCaption() : "");
        dateTimeLabel.setText("Date: " + photo.getDateTime().toString());
        
        // Initialize tag type combo with predefined types
        tagTypeCombo.getItems().addAll("location", "person");
        if (!tagTypeCombo.getItems().isEmpty()) {
            tagTypeCombo.setValue(tagTypeCombo.getItems().get(0));
        }
        
        refreshTags();
    }

    private void refreshTags() {
        tagsListView.getItems().clear();
        for (Tag t : currentPhoto.getTags()) {
            tagsListView.getItems().add(t.getType() + ": " + t.getValue());
        }
    }

    @FXML
    public void onZoomIn(ActionEvent ev) {
        zoomSlider.setValue(Math.min(zoomSlider.getMax(), zoomSlider.getValue() + 0.25));
    }

    @FXML
    public void onZoomOut(ActionEvent ev) {
        zoomSlider.setValue(Math.max(zoomSlider.getMin(), zoomSlider.getValue() - 0.25));
    }

    @FXML
    public void onResetZoom(ActionEvent ev) {
        zoomSlider.setValue(1.0);
    }

    @FXML
    public void onUpdateCaption(ActionEvent ev) {
        try {
            currentPhoto.setCaption(captionTextArea.getText());
            photoService.updatePhotoCaption(currentPhoto.getId(), captionTextArea.getText());
            try {
                photos48.ui.SceneManager.getDataStore().saveUser(tagService.getUser());
            } catch (Exception ex) {
                showError("Failed to persist caption: " + ex.getMessage());
            }
            showInfo("Caption updated");
        } catch (Exception e) {
            showError("Failed to update caption: " + e.getMessage());
        }
    }

    @FXML
    public void onAddTag(ActionEvent ev) {
        String type = tagTypeCombo.getValue();
        String value = tagValueField.getText();
        if (type == null || type.isBlank() || value == null || value.isBlank()) {
            showError("Enter type and value");
            return;
        }
        try {
            Tag t = new Tag(type, value);
            tagService.addTag(currentPhoto, t);
            tagValueField.clear();
            refreshTags();
            try {
                photos48.ui.SceneManager.getDataStore().saveUser(tagService.getUser());
            } catch (Exception ex) {
                showError("Failed to persist tag: " + ex.getMessage());
            }
            showInfo("Tag added");
        } catch (Exception e) {
            showError("Failed to add tag: " + e.getMessage());
        }
    }

    @FXML
    public void onRemoveTag(ActionEvent ev) {
        String selected = tagsListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Select a tag to remove");
            return;
        }
        String[] parts = selected.split(": ", 2);
        if (parts.length == 2) {
            try {
                Tag t = new Tag(parts[0], parts[1]);
                tagService.removeTag(currentPhoto, t);
                refreshTags();
                try {
                    photos48.ui.SceneManager.getDataStore().saveUser(tagService.getUser());
                } catch (Exception ex) {
                    showError("Failed to persist tag removal: " + ex.getMessage());
                }
                showInfo("Tag removed");
            } catch (Exception e) {
                showError("Failed to remove tag: " + e.getMessage());
            }
        }
    }

    @FXML
    public void onBack(ActionEvent ev) {
        // Return to album if we came from one, otherwise go to user home
        String albumName = Photos.getCurrentAlbum();
        if (albumName != null && !albumName.isEmpty()) {
            Photos.showAlbumView(albumName);
        } else {
            User user = tagService.getUser();
            Photos.showUserHome(user);
        }
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}

