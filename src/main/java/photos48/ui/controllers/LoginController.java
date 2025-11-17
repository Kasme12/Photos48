package photos48.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import photos48.model.User;
import photos48.service.AuthService;
import photos48.service.UserService;
import photos48.ui.Photos;
import photos48.ui.SceneManager;

/**
 * Controller for the login screen.
 * Handles user authentication and registration flow.
 * 
 * Responsibilities:
 * - Accept a username and either load an existing user or create a new one
 * - Route admin user to the admin UI and regular users to their home screen
 * - Display error alerts on failure
 * 
 * @author Esmeralda Bencosme
 * @author Armaan Sleem
 * @version 1.0
 */
public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private Button loginButton;

    private AuthService auth;
    private UserService userService;

    @FXML
    public void initialize() {
        auth = new AuthService(SceneManager.getDataStore());
        userService = new UserService(SceneManager.getDataStore());
    }

    @FXML
    public void onLogin(ActionEvent ev) {
        String username = usernameField.getText();
        if (username == null || username.isBlank()) {
            showError("Enter a username");
            return;
        }
        try {
            if ("admin".equals(username)) {
                // Load or create admin user
                User admin;
                if (auth.userExists(username)) {
                    admin = SceneManager.getDataStore().loadUser(username);
                } else {
                    admin = userService.createUser(username);
                }
                Photos.showAdminUI(admin);
            } else if (auth.userExists(username)) {
                // Existing user - load and show home
                User user = SceneManager.getDataStore().loadUser(username);
                Photos.showUserHome(user);
            } else {
                // New user - create and show home
                User user = userService.createUser(username);
                Photos.showUserHome(user);
            }
        } catch (Exception e) {
            showError("Login error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays an error alert with the provided message.
     * @param msg the error message to show
     */
    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}
