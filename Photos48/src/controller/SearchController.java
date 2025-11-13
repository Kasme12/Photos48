package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import model.Photo;
import model.Tag;
import model.Album;
import model.User;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles photo search by tag combinations and creates albums from results.
 */
public class SearchController {
    @FXML private TextField tagSearchField;
    @FXML private ListView<String> resultsList;

    private List<Photo> searchResults = new ArrayList<>();
    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = LoginController.currentUser;
    }

    @FXML
    private void searchByTag() {
        String query = tagSearchField.getText().trim();
        if (query.isEmpty()) {
            showAlert("Please enter a tag search query.");
            return;
        }

        searchResults.clear();
        resultsList.getItems().clear();

        String[] parts;
        boolean isOr = query.contains("OR");
        boolean isAnd = query.contains("AND");

        if (isOr) {
            parts = query.split("OR");
        } else if (isAnd) {
            parts = query.split("AND");
        } else {
            parts = new String[]{query};
        }

        for (Album album : currentUser.getAlbums()) {
            for (Photo photo : album.getPhotos()) {
                boolean match = false;
                if (parts.length == 1) {
                    match = photo.getTags().contains(parseTag(parts[0]));
                } else if (isOr) {
                    match = photo.getTags().contains(parseTag(parts[0])) || photo.getTags().contains(parseTag(parts[1]));
                } else if (isAnd) {
                    match = photo.getTags().contains(parseTag(parts[0])) && photo.getTags().contains(parseTag(parts[1]));
                }

                if (match && !searchResults.contains(photo)) {
                    searchResults.add(photo);
                    resultsList.getItems().add(photo.getFilePath());
                }
            }
        }

        if (searchResults.isEmpty()) {
            showAlert("No matching photos found.");
        }
    }

    @FXML
    private void createAlbumFromResults() {
        if (searchResults.isEmpty()) {
            showAlert("No search results to create album.");
            return;
        }

        Album newAlbum = new Album("SearchResults_" + System.currentTimeMillis());
        for (Photo photo : searchResults) {
            newAlbum.addPhoto(photo); // shared reference
        }

        currentUser.getAlbums().add(newAlbum);
        showAlert("Album created with " + searchResults.size() + " photos.");
    }

    private Tag parseTag(String raw) {
        String[] pair = raw.trim().split("=");
        return new Tag(pair[0].trim(), pair[1].trim());
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
