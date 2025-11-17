package photos48.ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import photos48.model.Album;
import photos48.model.User;
import photos48.service.AlbumService;
import photos48.service.UserService;
import photos48.ui.Photos;
import photos48.ui.SceneManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Controller for User Home (list albums).
 * @author Esmeralda Bencosme
 * @author Armaan Sleem
 */
public class UserHomeController {

    @FXML private ListView<String> albumListView;
    @FXML private VBox albumDetailsVBox;
    @FXML private Label albumNameLabel;
    @FXML private Label photoCountLabel;
    @FXML private Label dateRangeLabel;
    @FXML private Button addAlbumBtn;
    @FXML private CheckBox copyOnImportCheckbox;
    @FXML private Button renameAlbumBtn;
    @FXML private Button deleteAlbumBtn;
    @FXML private Button viewAlbumBtn;
    @FXML private Button logoutBtn;

    private User currentUser;
    private UserService userService;
    private AlbumService albumService;
    private ObservableList<String> albumList;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        userService = new UserService(SceneManager.getDataStore());
        albumService = new AlbumService(user);
        albumList = FXCollections.observableArrayList();
        albumListView.setItems(albumList);
        albumListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> onAlbumSelected());
        refreshAlbumList();
        // initialize copy-on-import checkbox
        copyOnImportCheckbox.setSelected(user.isCopyOnImport());
        copyOnImportCheckbox.selectedProperty().addListener((obs, oldV, newV) -> {
            user.setCopyOnImport(newV);
            try { userService.saveUser(user); } catch (Exception e) { showError("Failed to save setting: " + e.getMessage()); }
        });
    }

    private void refreshAlbumList() {
        albumList.clear();
        albumList.addAll(currentUser.getAlbums().keySet());
    }

    @FXML
    public void onAlbumSelected() {
        String albumName = albumListView.getSelectionModel().getSelectedItem();
        if (albumName == null) {
            albumDetailsVBox.setVisible(false);
            return;
        }
        Album album = currentUser.getAlbums().get(albumName);
        albumDetailsVBox.setVisible(true);
        albumNameLabel.setText("Album: " + albumName);
        photoCountLabel.setText("Photos: " + album.getPhotoCount());
        LocalDateTime earliest = album.getEarliest(currentUser.getPhotoStore());
        LocalDateTime latest = album.getLatest(currentUser.getPhotoStore());
        String dateRange = (earliest != null && latest != null)
                ? earliest + " to " + latest
                : "(no photos)";
        dateRangeLabel.setText("Date range: " + dateRange);
    }

    @FXML
    public void onAlbumListClicked(javafx.scene.input.MouseEvent ev) {
        // update selection details first
        onAlbumSelected();
        if (ev.getClickCount() == 2) {
            String albumName = albumListView.getSelectionModel().getSelectedItem();
            if (albumName != null) {
                Photos.showAlbumView(albumName);
            }
        }
    }

    @FXML
    public void onAddAlbum() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Album");
        dialog.setHeaderText("Enter album name:");
        dialog.setContentText("Name:");
        dialog.showAndWait().ifPresent(name -> {
                if (!name.isBlank()) {
                albumService.createAlbum(name);
                try {
                    userService.saveUser(currentUser);
                } catch (Exception e) {
                    showError("Failed to persist album: " + e.getMessage());
                }
                refreshAlbumList();
                showInfo("Album created: " + name);
            }
        });
    }

    @FXML
    public void onRenameAlbum() {
        String albumName = albumListView.getSelectionModel().getSelectedItem();
        if (albumName == null) {
            showError("Select an album");
            return;
        }
        TextInputDialog dialog = new TextInputDialog(albumName);
        dialog.setTitle("Rename Album");
        dialog.setHeaderText("Enter new name:");
        dialog.setContentText("New name:");
        dialog.showAndWait().ifPresent(newName -> {
            if (!newName.isBlank() && !newName.equals(albumName)) {
                try {
                    albumService.renameAlbum(albumName, newName);
                    try {
                        userService.saveUser(currentUser);
                    } catch (Exception e) {
                        showError("Failed to persist rename: " + e.getMessage());
                    }
                    refreshAlbumList();
                    showInfo("Album renamed to: " + newName);
                } catch (IllegalArgumentException e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    @FXML
    public void onDeleteAlbum() {
        String albumName = albumListView.getSelectionModel().getSelectedItem();
        if (albumName == null) { 
            showError("Select an album"); 
            return; 
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Album");
        confirm.setHeaderText("Are you sure?");
        confirm.setContentText("Delete album: " + albumName + "?");
        if (confirm.showAndWait().get() == ButtonType.OK) {
            albumService.deleteAlbum(albumName);
            try {
                userService.saveUser(currentUser);
            } catch (Exception e) {
                showError("Failed to persist delete: " + e.getMessage());
            }
            refreshAlbumList();
            showInfo("Album deleted");
        }
    }

    @FXML
    public void onViewAlbum() {
        String albumName = albumListView.getSelectionModel().getSelectedItem();
        if (albumName == null) { 
            showError("Select an album"); 
            return; 
        }
        Photos.showAlbumView(albumName);
    }

    @FXML
    public void onLogout() {
        // Save user data
        try {
            userService.saveUser(currentUser);
            Photos.showLogin();
        } catch (IOException e) {
            showError("Logout error: " + e.getMessage());
        }
    }

    private void showError(String msg) { new Alert(Alert.AlertType.ERROR, msg).showAndWait(); }
    private void showInfo(String msg) { new Alert(Alert.AlertType.INFORMATION, msg).showAndWait(); }
}
