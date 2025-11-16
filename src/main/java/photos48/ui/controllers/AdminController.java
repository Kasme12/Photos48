package photos48.ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import photos48.model.User;
import photos48.service.UserService;
import photos48.ui.Photos;
import photos48.ui.SceneManager;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the admin UI. Provides user management features:
 * - List existing users
 * - Create new users
 * - Delete users (except the admin account)
 * 
 * Uses UserService for persistence operations and presents results via a TableView.
 */
public class AdminController {

    @FXML private TableView<String> usersTable;
    @FXML private TableColumn<String, String> usernameCol;
    @FXML private TextField newUsernameField;
    @FXML private Button createUserBtn;
    @FXML private Button deleteUserBtn;
    @FXML private Button logoutBtn;

    private UserService userService;
    private ObservableList<String> usersList;

    @FXML
    public void initialize() {
        userService = new UserService(SceneManager.getDataStore());
        usersList = FXCollections.observableArrayList();
        usersTable.setItems(usersList);
        usernameCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue()));
        refreshUserList();
    }

    private void refreshUserList() {
        try {
            List<String> users = userService.listUsers();
            usersList.clear();
            usersList.addAll(users);
        } catch (IOException e) {
            showError("Failed to load users: " + e.getMessage());
        }
    }

    @FXML
    public void onCreateUser(ActionEvent ev) {
        String username = newUsernameField.getText();
        if (username == null || username.isBlank()) {
            showError("Enter a username");
            return;
        }
        try {
            userService.createUser(username);
            newUsernameField.clear();
            refreshUserList();
            showInfo("User created: " + username);
        } catch (IOException e) {
            showError("Create failed: " + e.getMessage());
        }
    }

    @FXML
    public void onDeleteUser(ActionEvent ev) {
        String selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Select a user to delete");
            return;
        }
        if ("admin".equals(selected)) {
            showError("Cannot delete admin user");
            return;
        }
        try {
            userService.deleteUser(selected);
            refreshUserList();
            showInfo("User deleted: " + selected);
        } catch (IOException e) {
            showError("Delete failed: " + e.getMessage());
        }
    }

    @FXML
    public void onLogout(ActionEvent ev) {
        Photos.showLogin();
    }

    /**
     * Shows an error alert with the supplied message.
     * @param msg the message to display
     */
    private void showError(String msg) { new Alert(Alert.AlertType.ERROR, msg).showAndWait(); }

    /**
     * Shows an informational alert with the supplied message.
     * @param msg the message to display
     */
    private void showInfo(String msg) { new Alert(Alert.AlertType.INFORMATION, msg).showAndWait(); }
}
