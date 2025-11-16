package photos48.service;

import photos48.model.User;
import photos48.persistence.DataStore;

import java.io.IOException;
import java.util.List;

/**
 * UserService provides user account management operations.
 * Handles creation and deletion of user accounts, as well as persistence of user data.
 * 
 * UserService works closely with DataStore to maintain a list of all users and save/load
 * individual user accounts. When a user is created, a new User object is initialized and
 * saved to persistent storage. When a user is deleted, their account is removed from the
 * user list.
 * 
 * Note: The "admin" user is a special administrative account for user management and should
 * not be deleted through normal operations.
 * 
 * @author Esmeralda Bencosme
 * @version 1.0
 */
public class UserService {
    /** Reference to the DataStore for persistence operations */
    private final DataStore ds;

    /**
     * Constructs a UserService with the given DataStore.
     * @param ds the DataStore to use for persistence operations
     */
    public UserService(DataStore ds) { this.ds = ds; }

    /**
     * Retrieves a list of all user usernames.
     * @return a list of username strings
     * @throws IOException if a persistence error occurs
     */
    public List<String> listUsers() throws IOException { return ds.listUsers(); }

    /**
     * Creates a new user account with the given username.
     * If a user with this username already exists, throws an IOException.
     * The new user is initialized with no albums and is persisted to storage.
     * @param username the username for the new user (must be unique)
     * @return the newly created User object
     * @throws IOException if the username already exists or a persistence error occurs
     */
    public User createUser(String username) throws IOException {
        List<String> users = ds.listUsers();
        if (users.contains(username)) throw new IOException("User exists");
        users.add(username);
        ds.saveUsersList(users);
        User newUser = new User(username);
        ds.saveUser(newUser);
        return newUser;
    }

    /**
     * Deletes a user account.
     * Removes the user from the user list and deletes their persisted data.
     * @param username the username of the account to delete
     * @throws IOException if a persistence error occurs
     */
    public void deleteUser(String username) throws IOException {
        List<String> users = ds.listUsers();
        users.remove(username);
        ds.saveUsersList(users);
    }

    /**
     * Saves a user's current state to persistent storage.
     * Call this method after modifying a user's albums, photos, or other data to ensure
     * the changes are persisted.
     * @param u the User to save
     * @throws IOException if a persistence error occurs
     */
    public void saveUser(User u) throws IOException { ds.saveUser(u); }
}

