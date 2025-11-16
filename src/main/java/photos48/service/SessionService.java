package photos48.service;

import photos48.model.User;
import photos48.persistence.DataStore;

import java.io.IOException;

/**
 * SessionService manages the current logged-in user and handles save on logout/quit.
 * Provides a central point for session management and user data persistence.
 * 
 * @author Esmeralda Bencosme
 * @version 1.0
 */
public class SessionService {
    private static SessionService instance;
    private User currentUser;
    private DataStore dataStore;

    private SessionService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * Gets or creates the SessionService singleton.
     * @param dataStore the DataStore for persistence
     * @return the SessionService instance
     */
    public static SessionService getInstance(DataStore dataStore) {
        if (instance == null) {
            instance = new SessionService(dataStore);
        }
        return instance;
    }

    /**
     * Sets the current logged-in user.
     * @param user the User object
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Gets the current logged-in user.
     * @return the current User, or null if not logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Saves the current user's data and clears the session.
     * Called on logout.
     * @throws IOException if save fails
     */
    public void logout() throws IOException {
        if (currentUser != null) {
            dataStore.saveUser(currentUser);
            currentUser = null;
        }
    }

    /**
     * Checks if a user is currently logged in.
     * @return true if currentUser is not null
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
