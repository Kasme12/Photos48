package photos48.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.concurrent.Task;
import javafx.application.Platform;
import photos48.model.Album;
import photos48.model.Photo;
import photos48.model.User;
import photos48.service.AlbumService;
import photos48.service.PhotoService;
import photos48.ui.Photos;
import photos48.ui.SceneManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.UUID;
import java.util.Optional;


/**
 * Controller for Album view (photo grid, add/remove/move/copy/slideshow).
 * @author Photos48
 */
public class AlbumController {

    @FXML private Label albumTitleLabel;
    @FXML private FlowPane photoGridPane;
    @FXML private Button addPhotoBtn, removePhotoBtn, viewPhotoBtn, copyPhotoBtn, movePhotoBtn;
    @FXML private Button prevPhotoBtn, nextPhotoBtn, searchBtn, backBtn;
    @FXML private ProgressBar copyProgressBar;
    @FXML private Label copyProgressLabel;

    private User currentUser;
    private String currentAlbumName;
    private Album currentAlbum;
    private AlbumService albumService;
    private PhotoService photoService;
    private int slideIndex = 0;
    private UUID selectedPhotoId = null;
    private java.util.Map<String, Image> thumbnailCache = new java.util.HashMap<>();

    public void setContext(User user, String albumName) {
        this.currentUser = user;
        this.currentAlbumName = albumName;
        this.currentAlbum = user.getAlbums().get(albumName);
        this.albumService = new AlbumService(user);
        this.photoService = new PhotoService(user);
        albumTitleLabel.setText("Album: " + albumName);
        refreshPhotoGrid();
    }

    private void refreshPhotoGrid() {
        photoGridPane.getChildren().clear();
        selectedPhotoId = null;
        Path workspace = SceneManager.getDataStore().getWorkspaceDir();
        Path thumbDir = workspace.resolve(".thumbnails").resolve("user_" + currentUser.getUsername());
        try { Files.createDirectories(thumbDir); } catch (Exception _e) { /* ignore */ }

        for (UUID id : currentAlbum.getPhotoIds()) {
            Photo p = currentUser.getPhotoStore().get(id);
            if (p == null) continue;
            String caption = p.getCaption() != null && !p.getCaption().isEmpty() ? p.getCaption() : "Photo";

            // compute cache filename from path hash and last-modified to force regen when source changes
            long lastMod = 0L;
            try { java.io.File srcf = new java.io.File(p.getPath()); if (srcf.exists()) lastMod = srcf.lastModified(); } catch (Exception _e) { /* ignore */ }
            String cacheName = Integer.toHexString(java.util.Objects.hash(p.getPath(), lastMod)) + ".png";
            Path cacheFile = thumbDir.resolve(cacheName);

            ImageView iv = new ImageView();
            iv.setFitWidth(150);
            iv.setPreserveRatio(true);

            // if disk cache exists, load from it (background loading)
            if (Files.exists(cacheFile)) {
                Image thumb = new Image(cacheFile.toUri().toString(), 150, 0, true, true, true);
                thumbnailCache.put(p.getPath(), thumb);
                iv.setImage(thumb);
            } else {
                // if we already have in-memory cache use it
                Image thumb = thumbnailCache.get(p.getPath());
                if (thumb != null) {
                    iv.setImage(thumb);
                } else {
                    // placeholder while generating
                    try {
                        Image placeholder = new Image(new File("data/stock/stock1.png").toURI().toString(), 150, 0, true, true, true);
                        iv.setImage(placeholder);
                    } catch (Exception ex) { /* ignore */ }

                    // generate thumbnail in background and save to disk for persistence
                    final ImageView ivRef = iv;
                    final Path cacheFileRef = cacheFile;
                    final Photo photoRef = p;
                    new Thread(() -> {
                        try {
                            File src = new File(photoRef.getPath());
                            if (!src.exists()) return;
                            java.awt.image.BufferedImage srcImg = javax.imageio.ImageIO.read(src);
                            if (srcImg == null) return;
                            int targetW = 150;
                            int srcW = srcImg.getWidth();
                            int srcH = srcImg.getHeight();
                            int targetH = Math.max(1, (int)((double)srcH * targetW / srcW));
                            java.awt.image.BufferedImage dst = new java.awt.image.BufferedImage(targetW, targetH, java.awt.image.BufferedImage.TYPE_INT_RGB);
                            java.awt.Graphics2D g = dst.createGraphics();
                            g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                            g.drawImage(srcImg, 0, 0, targetW, targetH, null);
                            g.dispose();
                            try { javax.imageio.ImageIO.write(dst, "png", cacheFileRef.toFile()); } catch (Exception ex) { /* ignore */ }
                            Image generated = new Image(cacheFileRef.toUri().toString(), 150, 0, true, true, true);
                            thumbnailCache.put(photoRef.getPath(), generated);
                            Platform.runLater(() -> ivRef.setImage(generated));
                        } catch (Exception ex) {
                            // ignore generation errors
                        }
                    }).start();
                }
            }

            Button btn = new Button();
            btn.setGraphic(iv);
            btn.setContentDisplay(ContentDisplay.TOP);
            btn.setText(caption);
            btn.setStyle("-fx-padding: 6; -fx-min-width: 120; -fx-min-height: 120; -fx-wrap-text: true;");
            btn.setOnAction(e -> selectPhoto(id, btn));
            photoGridPane.getChildren().add(btn);
        }
    }

    private void selectPhoto(UUID id, Button btn) {
        selectedPhotoId = id;
        // Visual feedback
        for (var child : photoGridPane.getChildren()) {
            ((Button)child).setStyle("-fx-padding: 10; -fx-min-width: 100; -fx-min-height: 80; -fx-wrap-text: true;");
        }
        btn.setStyle("-fx-padding: 10; -fx-min-width: 100; -fx-min-height: 80; -fx-wrap-text: true; -fx-border-color: blue; -fx-border-width: 2;");
    }

    @FXML
    public void onAddPhoto(ActionEvent ev) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select Photo File");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp"));
        File file = fc.showOpenDialog(addPhotoBtn.getScene().getWindow());
        
        if (file != null && file.exists()) {
            // background copy with progress reporting
            addPhotoBtn.setDisable(true);
            Task<Path> task = new Task<Path>() {
                @Override
                protected Path call() throws Exception {
                    Path workspace = SceneManager.getDataStore().getWorkspaceDir();
                    Path userPhotosDir = workspace.resolve("user_" + currentUser.getUsername()).resolve("photos");
                    Files.createDirectories(userPhotosDir);
                    String destName = System.currentTimeMillis() + "_" + file.getName();
                    Path dest = userPhotosDir.resolve(destName);
                    if (currentUser.isCopyOnImport()) {
                        long total = Files.size(file.toPath());
                        try (InputStream in = Files.newInputStream(file.toPath()); OutputStream out = Files.newOutputStream(dest, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                            byte[] buf = new byte[8192];
                            long copied = 0;
                            int r;
                            while ((r = in.read(buf)) != -1) {
                                out.write(buf, 0, r);
                                copied += r;
                                updateProgress(copied, total);
                            }
                        }
                        return dest;
                    } else {
                        updateProgress(1, 1);
                        return file.toPath();
                    }
                }
            };

            copyProgressBar.setVisible(true);
            copyProgressLabel.setVisible(true);
            copyProgressLabel.setText("Copying...");
            copyProgressBar.progressProperty().bind(task.progressProperty());

            task.setOnSucceeded(ev2 -> {
                try {
                    Path result = task.getValue();
                    Photo photo = new Photo(result.toAbsolutePath().toString(), file.getName());
                    photoService.addPhoto(photo);
                    albumService.addPhotoToAlbum(currentAlbumName, photo.getId());
                    try { SceneManager.getDataStore().saveUser(currentUser); } catch (Exception ex) { showError("Failed to persist photo add: " + ex.getMessage()); }
                    refreshPhotoGrid();
                    showInfo("Photo added successfully");
                } finally {
                    addPhotoBtn.setDisable(false);
                    copyProgressBar.progressProperty().unbind();
                    copyProgressBar.setVisible(false);
                    copyProgressLabel.setVisible(false);
                    copyProgressLabel.setText("");
                }
            });

            task.setOnFailed(ev2 -> {
                addPhotoBtn.setDisable(false);
                copyProgressBar.progressProperty().unbind();
                copyProgressBar.setVisible(false);
                copyProgressLabel.setVisible(false);
                copyProgressLabel.setText("");
                Throwable ex = task.getException();
                showError("Failed to import photo: " + (ex != null ? ex.getMessage() : "unknown"));
            });

            Thread t = new Thread(task, "photo-import");
            t.setDaemon(true);
            t.start();
        }
    }

    @FXML
    public void onRemovePhoto(ActionEvent ev) {
        if (selectedPhotoId == null) { 
            showError("Select a photo"); 
            return; 
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Remove Photo");
        confirm.setHeaderText("Confirm Remove");
        confirm.setContentText("Remove this photo from the album?");
            if (confirm.showAndWait().get() == ButtonType.OK) {
                // remove from album
                albumService.removePhotoFromAlbum(currentAlbumName, selectedPhotoId);
                try { SceneManager.getDataStore().saveUser(currentUser); } catch (Exception e) { showError("Failed to persist album change: " + e.getMessage()); }
                refreshPhotoGrid();

                // check for orphaned file and offer recycle
                try {
                    Path orphan = photoService.findOrphanFile(selectedPhotoId);
                    if (orphan != null && Files.exists(orphan)) {
                        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                        a.setTitle("Orphaned File Detected");
                        a.setHeaderText("The photo file appears to be orphaned in your workspace.");
                        a.setContentText("Do you want to move it to the recycle folder?");
                        Optional<ButtonType> res = a.showAndWait();
                        if (res.isPresent() && res.get() == ButtonType.OK) {
                            Path trash = SceneManager.getDataStore().getWorkspaceDir().resolve(".trash").resolve("user_" + currentUser.getUsername());
                            Files.createDirectories(trash);
                            Path dest = trash.resolve(orphan.getFileName());
                            Files.move(orphan, dest, StandardCopyOption.REPLACE_EXISTING);
                            // remove photo record from store since file moved
                            photoService.removePhotoFromStore(selectedPhotoId);
                            try { SceneManager.getDataStore().saveUser(currentUser); } catch (Exception ex) { showError("Failed saving after recycle: " + ex.getMessage()); }
                        }
                    }
                } catch (Exception ex) {
                    showError("Error while checking/moving orphan file: " + ex.getMessage());
                }
        }
    }

    @FXML
    public void onViewPhoto(ActionEvent ev) {
        if (selectedPhotoId == null) { 
            showError("Select a photo"); 
            return; 
        }
        Photos.showPhotoViewer(selectedPhotoId);
    }

    @FXML
    public void onCopyPhoto(ActionEvent ev) {
        if (selectedPhotoId == null) { 
            showError("Select a photo"); 
            return; 
        }
        // Show album selection dialog
        var albums = currentUser.getAlbums().keySet();
        if (albums.isEmpty()) {
            showError("No other albums");
            return;
        }
        
        ChoiceDialog<String> dialog = new ChoiceDialog<>(
            albums.iterator().next(),
            albums
        );
        dialog.setTitle("Copy Photo");
        dialog.setHeaderText("Select target album:");
        dialog.showAndWait().ifPresent(albumName -> {
            if (!albumName.equals(currentAlbumName)) {
                albumService.copyPhotoAcrossAlbums(currentAlbumName, albumName, selectedPhotoId);
                try {
                    SceneManager.getDataStore().saveUser(currentUser);
                } catch (Exception e) {
                    showError("Failed to persist photo copy: " + e.getMessage());
                }
                showInfo("Photo copied");
            } else {
                showError("Cannot copy to same album");
            }
        });
    }

    @FXML
    public void onMovePhoto(ActionEvent ev) {
        if (selectedPhotoId == null) { 
            showError("Select a photo"); 
            return; 
        }
        var albums = currentUser.getAlbums().keySet();
        if (albums.isEmpty()) {
            showError("No other albums");
            return;
        }
        
        ChoiceDialog<String> dialog = new ChoiceDialog<>(
            albums.iterator().next(),
            albums
        );
        dialog.setTitle("Move Photo");
        dialog.setHeaderText("Select target album:");
        dialog.showAndWait().ifPresent(albumName -> {
            if (!albumName.equals(currentAlbumName)) {
                albumService.movePhotoAcrossAlbums(currentAlbumName, albumName, selectedPhotoId);
                try {
                    SceneManager.getDataStore().saveUser(currentUser);
                } catch (Exception e) {
                    showError("Failed to persist photo move: " + e.getMessage());
                }
                refreshPhotoGrid();
                showInfo("Photo moved");
            } else {
                showError("Cannot move to same album");
            }
        });
    }

    @FXML
    public void onPrevPhoto(ActionEvent ev) {
        if (currentAlbum.getPhotoIds().isEmpty()) return;
        slideIndex = (slideIndex - 1 + currentAlbum.getPhotoIds().size()) % currentAlbum.getPhotoIds().size();
        showSlide();
    }

    @FXML
    public void onNextPhoto(ActionEvent ev) {
        if (currentAlbum.getPhotoIds().isEmpty()) return;
        slideIndex = (slideIndex + 1) % currentAlbum.getPhotoIds().size();
        showSlide();
    }

    private void showSlide() {
        var photoIds = currentAlbum.getPhotoIds();
        if (!photoIds.isEmpty()) {
            UUID photoId = photoIds.get(slideIndex);
            Photos.showPhotoViewer(photoId);
        }
    }

    @FXML
    public void onSearch(ActionEvent ev) {
        Photos.showSearch();
    }

    @FXML
    public void onBack(ActionEvent ev) {
        // Go back to user home
        User user = currentUser;
        if (user != null) {
            // Refresh to get updated data
            try {
                user = SceneManager.getDataStore().loadUser(user.getUsername());
            } catch (Exception e) {
                // Use current user if reload fails
            }
            Photos.showUserHome(user);
        }
    }

    private void showError(String msg) { new Alert(Alert.AlertType.ERROR, msg).showAndWait(); }
    private void showInfo(String msg) { new Alert(Alert.AlertType.INFORMATION, msg).showAndWait(); }
}
