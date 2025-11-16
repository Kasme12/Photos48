package photos48.persistence;

import photos48.model.User;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * DataStore defines the contract for persistent data storage operations.
 * 
 * DataStore abstracts the persistence mechanism, allowing different implementations
 * (e.g., ObjectDataStore using Java serialization, future implementations using databases).
 *
 * Responsibilities:
 * - Maintain a list of all user usernames
 * - Load/save individual User objects (with all their albums and photos)
 * - Work with a workspace directory (e.g., ~/.photos48/)
 *
 * Implementations must handle:
 * - User list persistence (list of usernames)
 * - Per-user data persistence (User objects with albums, photos, tags)
 * - IOException for I/O errors
 * - ClassNotFoundException for deserialization errors
 */
public interface DataStore {
    /**
     * Retrieves the list of all user usernames.
     * @return a list of username strings
     * @throws IOException if a persistence error occurs during retrieval
     */
    List<String> listUsers() throws IOException;

    /**
     * Saves the list of all user usernames.
     * This is called whenever a user is created or deleted.
     * @param users the list of usernames to save
     * @throws IOException if a persistence error occurs during saving
     */
    void saveUsersList(List<String> users) throws IOException;

    /**
     * Loads a user account from persistent storage.
     * Retrieves all user data including albums, photos, and tags.
     * @param username the username of the user to load
     * @return the User object with all data restored
     * @throws IOException if a persistence error occurs
     * @throws ClassNotFoundException if the serialized data is incompatible
     */
    User loadUser(String username) throws IOException, ClassNotFoundException;

    /**
     * Saves a user account to persistent storage.
     * Persists all user data including albums, photos, captions, and tags.
     * @param user the User object to save
     * @throws IOException if a persistence error occurs during saving
     */
    void saveUser(User user) throws IOException;

    /**
     * Returns the filesystem path to the application's workspace directory.
     * Example: ~/.photos48
     * @return Path to workspace directory
     */
    Path getWorkspaceDir();
}
