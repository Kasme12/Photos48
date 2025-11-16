package photos48.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import photos48.model.Album;
import photos48.model.Photo;
import photos48.model.Tag;
import photos48.model.User;
import photos48.service.AlbumService;
import photos48.service.SearchService;
import photos48.ui.Photos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller for Search (by date range or tag pairs: single, AND, OR).
 * @author Esmeralda Bencosme
 */
public class SearchController {

    @FXML private DatePicker fromDatePicker, toDatePicker;
    @FXML private ComboBox<String> searchModeCombo, tag1TypeCombo, tag2TypeCombo;
    @FXML private TextField tag1ValueField, tag2ValueField;
    @FXML private ListView<String> resultsListView;
    @FXML private Button backBtn;

    private User currentUser;
    private SearchService searchService;
    private AlbumService albumService;
    private List<Photo> currentResults = null;
    private java.util.Map<Integer, Photo> resultsMap = new java.util.HashMap<>();

    public void setContext(User user) {
        this.currentUser = user;
        this.searchService = new SearchService(user.getPhotoStore());
        this.albumService = new AlbumService(user);
        
        // Initialize search mode combo
        searchModeCombo.getItems().addAll("Single", "AND", "OR");
        searchModeCombo.setValue("Single");
        
        // Initialize tag type combos
        // populate from user's tagTypes so custom types show up
        tag1TypeCombo.getItems().clear();
        tag2TypeCombo.getItems().clear();
        if (user.getTagTypes() != null && !user.getTagTypes().isEmpty()) {
            tag1TypeCombo.getItems().addAll(user.getTagTypes().keySet());
            tag2TypeCombo.getItems().addAll(user.getTagTypes().keySet());
            if (!tag1TypeCombo.getItems().isEmpty()) tag1TypeCombo.setValue(tag1TypeCombo.getItems().get(0));
            if (!tag2TypeCombo.getItems().isEmpty()) tag2TypeCombo.setValue(tag2TypeCombo.getItems().get(0));
        } else {
            tag1TypeCombo.getItems().addAll("location", "person");
            tag2TypeCombo.getItems().addAll("location", "person");
        }
    }

    @FXML
    public void onResultClicked(javafx.scene.input.MouseEvent ev) {
        if (ev.getClickCount() == 2) {
            int idx = resultsListView.getSelectionModel().getSelectedIndex();
            if (idx < 0) return;
            Photo p = resultsMap.get(idx);
            if (p != null) {
                try {
                    Photos.showPhotoViewer(p.getId());
                } catch (Exception e) {
                    showError("Failed to open photo: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    public void onSearchByDate(ActionEvent ev) {
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();
        if (fromDate == null || toDate == null) {
            showError("Select both dates");
            return;
        }
        LocalDateTime from = fromDate.atStartOfDay();
        LocalDateTime to = toDate.atTime(LocalTime.MAX);
        currentResults = searchService.byDateRange(from, to);
        displayResults(currentResults);
        showInfo("Found " + currentResults.size() + " photos");
    }

    @FXML
    public void onSearchByTags(ActionEvent ev) {
        String mode = searchModeCombo.getValue();
        String type1 = tag1TypeCombo.getValue();
        String val1 = tag1ValueField.getText();
        if (mode == null || type1 == null || val1 == null || val1.isBlank()) {
            showError("Fill in required fields");
            return;
        }

        List<Photo> results = null;
        if ("Single".equals(mode)) {
            results = searchService.bySingleTag(type1, val1);
        } else {
            String type2 = tag2TypeCombo.getValue();
            String val2 = tag2ValueField.getText();
            if (type2 == null || val2 == null || val2.isBlank()) {
                showError("Fill in Tag 2 for AND/OR");
                return;
            }
            Tag a = new Tag(type1, val1);
            Tag b = new Tag(type2, val2);
            if ("AND".equals(mode)) {
                results = searchService.byAnd(a, b);
            } else {
                results = searchService.byOr(a, b);
            }
        }
        if (results != null) {
            currentResults = results;
            displayResults(results);
            showInfo("Found " + results.size() + " photos");
        }
    }

    private void displayResults(List<Photo> results) {
        resultsListView.getItems().clear();
        resultsMap.clear();
        int idx = 0;
        for (Photo p : results) {
            resultsMap.put(idx, p);
            String caption = (p.getCaption() != null && !p.getCaption().isEmpty()) ? p.getCaption() : "(no caption)";
            resultsListView.getItems().add((idx + 1) + ". " + caption);
            idx++;
        }
    }

    @FXML
    public void onCreateAlbum(ActionEvent ev) {
        if (currentResults == null || currentResults.isEmpty()) {
            showError("No results to create album from");
            return;
        }
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Album from Results");
        dialog.setHeaderText("Album name:");
        dialog.setContentText("Name:");
        dialog.showAndWait().ifPresent(name -> {
            if (!name.isBlank()) {
                try {
                    albumService.createAlbum(name);
                    // Add all result photos to album
                    for (Photo p : currentResults) {
                        albumService.addPhotoToAlbum(name, p.getId());
                    }
                    try {
                        photos48.ui.SceneManager.getDataStore().saveUser(currentUser);
                    } catch (Exception ex) {
                        showError("Failed to persist album from results: " + ex.getMessage());
                    }
                    showInfo("Album '" + name + "' created with " + currentResults.size() + " photos");
                } catch (Exception e) {
                    showError("Failed to create album: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    public void onBack(ActionEvent ev) {
        // Return to album if we came from one, otherwise go to user home
        String albumName = Photos.getCurrentAlbum();
        if (albumName != null && !albumName.isEmpty()) {
            Photos.showAlbumView(albumName);
        } else {
            User user = currentUser;
            if (user != null) {
                Photos.showUserHome(user);
            }
        }
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}

