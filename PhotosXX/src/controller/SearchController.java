package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SearchController {
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField tag1Field;
    @FXML
    private TextField tag2Field;
    @FXML
    private ChoiceBox<String> logicChoice;
    @FXML
    private ListView<String> resultsList;

    private ObservableList<String> results;
    private List<Photo> matchedPhotos;
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        logicChoice.setValue("AND");
        results = FXCollections.observableArrayList();
        resultsList.setItems(results);
        matchedPhotos = new ArrayList<>();
    }

    @FXML
    private void handleSearchByDate() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        if (start == null || end == null || start.isAfter(end)) {
            showAlert("Invalid date range.");
            return;
        }

        results.clear();
        matchedPhotos.clear();

        for (Album album : currentUser.getAlbums()) {
            for (Photo photo : album.getPhotos()) {
                LocalDate date = photo.getDateTaken().toLocalDate();
                if (!date.isBefore(start) && !date.isAfter(end)) {
                    if (!matchedPhotos.contains(photo)) {
                        matchedPhotos.add(photo);
                        results.add(photo.getFilePath());
                    }
                }
            }
        }

        if (results.isEmpty()) showAlert("No photos found in date range.");
    }

    @FXML
    private void handleSearchByTags() {
        String raw1 = tag1Field.getText().trim();
        String raw2 = tag2Field.getText().trim();
        String logic = logicChoice.getValue();

        if (raw1.isEmpty()) {
            showAlert("At least one tag is required.");
            return;
        }

        Tag tag1 = parseTag(raw1);
        Tag tag2 = raw2.isEmpty() ? null : parseTag(raw2);

        results.clear();
        matchedPhotos.clear();

        for (Album album : currentUser.getAlbums()) {
            for (Photo photo : album.getPhotos()) {
                boolean match1 = photo.getTags().contains(tag1);
                boolean match2 = tag2 == null || photo.getTags().contains(tag2);

                boolean matched = logic.equals("AND") ? (match1 && match2) : (match1 || match2);
                if (matched && !matchedPhotos.contains(photo)) {
                    matchedPhotos.add(photo);
                    results.add(photo.getFilePath());
                }
            }
        }

        if (results.isEmpty()) showAlert("No photos matched the tags.");
    }

    @FXML
    private void handleCreateAlbum() {
        if (matchedPhotos.isEmpty()) {
            showAlert("No photos to create album.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Album");
        dialog.setHeaderText("Enter name for new album:");
        dialog.setContentText("Album name:");
        dialog.showAndWait().ifPresent(name -> {
            if (name.trim().isEmpty()) {
                showAlert("Album name cannot be empty.");
                return;
            }
            if (currentUser.getAlbum(name) != null) {
                showAlert("Album already exists.");
                return;
            }
            Album newAlbum = new Album(name);
            for (Photo p : matchedPhotos) {
                newAlbum.addPhoto(p);
            }
            currentUser.getAlbums().add(newAlbum);
            showAlert("Album '" + name + "' created with " + matchedPhotos.size() + " photos.");
        });
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user.fxml"));
            Scene scene = new Scene(loader.load());
            UserController controller = loader.getController();
            controller.setUser(currentUser);
            Stage stage = (Stage) resultsList.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Welcome " + currentUser.getUsername());
        } catch (Exception e) {
            showAlert("Error returning to user dashboard.");
        }
    }

    private Tag parseTag(String raw) {
        String[] parts = raw.split("=");
        if (parts.length != 2) return new Tag("invalid", "invalid");
        return new Tag(parts[0].trim(), parts[1].trim());
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Search");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
