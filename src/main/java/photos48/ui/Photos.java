package photos48.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import photos48.model.User;
import photos48.persistence.ObjectDataStore;
import photos48.service.StockService;

import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import javafx.scene.control.TextArea;
import java.net.URL;

/**
 * Main JavaFX application entry point for Photos48.
 * 
 * Responsibilities:
 * - Initialize JavaFX application and load FXML scenes
 * - Manage scene transitions (Login → Admin/UserHome → Album → PhotoViewer/Search)
 * - Initialize persistence layer (ObjectDataStore)
 * - Initialize stock user and images on first launch
 * - Handle application shutdown with data save
 * 
 * Scene Methods:
 * - showLogin(): Display login/registration screen
 * - showAdminUI(User): Display admin panel for user management
 * - showUserHome(User): Display user's album list
 * - showAlbumView(String): Display photos in album
 * - showPhotoViewer(UUID): Display single photo with tags
 * - showSearch(): Display search interface
 * 
 * @author Esmeralda Bencosme
 * @version 1.0
 */
public class Photos extends Application {

    private static Stage primaryStage;
    private static User currentUser;

    /**
     * Main entry point for the application.
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the JavaFX application, sets up persistence, and shows the login screen.
     * @param stage the primary stage
     * @throws Exception if initialization fails
     */
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        try {
            ObjectDataStore ds = new ObjectDataStore();
            SceneManager.init(ds);
            new StockService(ds).initStockIfNeeded();
            primaryStage.setOnCloseRequest(e -> onAppClose());
            showLogin();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to show login: " + e.getMessage()).showAndWait();
        }
    }

    /**
     * Shows the login/registration screen.
     * Clears the current user session.
     */
    public static void showLogin() {
        try {
            currentUser = null;
            URL fxmlUrl = Photos.class.getResource("/fxml/login.fxml");
            if (fxmlUrl == null) throw new IOException("Cannot find login.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            primaryStage.setTitle("Photos48 - Login");
            primaryStage.setScene(new Scene(root, 400, 250));
            primaryStage.show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to show admin UI: " + e.getMessage()).showAndWait();
        }
    }

    /**
     * Shows the admin panel for user management.
     * Only available if username is "admin".
     * @param admin the admin User object
     */
    public static void showAdminUI(User admin) {
        try {
            currentUser = admin;
            URL fxmlUrl = Photos.class.getResource("/fxml/admin.fxml");
            if (fxmlUrl == null) throw new IOException("Cannot find admin.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            primaryStage.setTitle("Photos48 - Admin Panel");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to show user home: " + e.getMessage()).showAndWait();
        }
    }

    /**
     * Shows the user home screen with their albums.
     * @param user the logged-in User object
     */
    public static void showUserHome(User user) {
        try {
            currentUser = user;
            URL fxmlUrl = Photos.class.getResource("/fxml/user_home.fxml");
            if (fxmlUrl == null) throw new IOException("Cannot find user_home.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            photos48.ui.controllers.UserHomeController ctrl = loader.getController();
            ctrl.setCurrentUser(user);
            primaryStage.setTitle("Photos48 - " + user.getUsername());
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to show album view: " + e.getMessage()).showAndWait();
        }
    }

    /**
     * Shows the album view with photos.
     * @param albumName the name of the album to display
     */
    public static void showAlbumView(String albumName) {
        try {
            URL fxmlUrl = Photos.class.getResource("/fxml/album.fxml");
            if (fxmlUrl == null) throw new IOException("Cannot find album.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            photos48.ui.controllers.AlbumController ctrl = loader.getController();
            ctrl.setContext(currentUser, albumName);
            primaryStage.setTitle("Photos48 - Album: " + albumName);
            primaryStage.setScene(new Scene(root, 900, 700));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // show full stack trace in expandable alert so user can copy it
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionText = sw.toString();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to show album view: " + e.getMessage());
            TextArea ta = new TextArea(exceptionText);
            ta.setEditable(false);
            ta.setWrapText(false);
            ta.setMaxWidth(Double.MAX_VALUE);
            ta.setMaxHeight(Double.MAX_VALUE);
            alert.getDialogPane().setExpandableContent(ta);
            alert.showAndWait();
        }
    }

    /**
     * Shows the photo viewer for a single photo.
     * @param photoId the UUID of the photo to display
     */
    public static void showPhotoViewer(java.util.UUID photoId) {
        try {
            URL fxmlUrl = Photos.class.getResource("/fxml/photo_viewer.fxml");
            if (fxmlUrl == null) throw new IOException("Cannot find photo_viewer.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            photos48.ui.controllers.PhotoViewerController ctrl = loader.getController();
            photos48.model.Photo photo = currentUser.getPhotoStore().get(photoId);
            ctrl.setPhoto(photo, new photos48.service.TagService(currentUser));
            primaryStage.setTitle("Photos48 - Photo Viewer");
            primaryStage.setScene(new Scene(root, 800, 700));
            primaryStage.show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to show search: " + e.getMessage()).showAndWait();
        }
    }

        /**
         * Shows the search interface for querying photos.
         * Loads the search FXML and initializes SearchController with the current user's context.
         * Allows multi-mode search: date range, single tag, AND/OR combinations.
         */
    public static void showSearch() {
        try {
            URL fxmlUrl = Photos.class.getResource("/fxml/search.fxml");
            if (fxmlUrl == null) throw new IOException("Cannot find search.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            photos48.ui.controllers.SearchController ctrl = loader.getController();
            ctrl.setContext(currentUser);
            primaryStage.setTitle("Photos48 - Search");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        /**
         * Retrieves the currently logged-in user.
         * @return the current User, or null if no user is logged in
         */
    public static User getCurrentUser() {
        return currentUser;
    }

        /**
         * Handles application shutdown and data persistence.
         * Saves the current user's data to persistent storage before closing the application.
         * Ensures that album changes, photo tags, and captions persist across sessions.
         * Does not save "admin" user data (admin account is only for user management).
         */
    private void onAppClose() {
        // Save current user before closing
        if (currentUser != null && !currentUser.getUsername().equals("admin")) {
            try {
                SceneManager.getDataStore().saveUser(currentUser);
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to save data on exit: " + e.getMessage()).showAndWait();
            }
        }
    }
}

