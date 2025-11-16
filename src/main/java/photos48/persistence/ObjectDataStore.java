package photos48.persistence;

import photos48.model.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * ObjectDataStore implements the DataStore interface using Java Object Serialization.
 * 
 * This implementation uses ObjectOutputStream/ObjectInputStream to persist data to the
 * user's workspace directory at ~/.photos48/ (on Unix/Linux/Mac) or the Windows equivalent.
 *
 * Persistence Strategy:
 * - User list: Stored in ~/.photos48/users.ser (List<String> of usernames)
 * - Per-user data: Stored in ~/.photos48/user_<username>.ser (User object with all data)
 *
 * Serialization:
 * - All model classes (User, Album, Photo, Tag, TagType) implement Serializable
 * - Each class has a serialVersionUID for version compatibility
 * - ObjectInputStream/ObjectOutputStream handle serialization automatically
 *
 * The workspace directory is created on first initialization if it does not exist.
 * User data is fully restored on load, including album references and photo tags.
 */
public class ObjectDataStore implements DataStore {

    /** The workspace directory where all data is persisted (~/.photos48/) */
    private final Path workspaceDir;
    
    /** The file where the list of all usernames is stored (users.ser) */
    private final Path usersIndex;

    /**
     * Constructs an ObjectDataStore and initializes the workspace directory.
     * Creates ~/.photos48/ if it does not exist.
     * @throws IOException if the workspace directory cannot be created
     */
    public ObjectDataStore() throws IOException {
        String home = System.getProperty("user.home");
        workspaceDir = Path.of(home, ".photos48");
        if (!Files.exists(workspaceDir)) Files.createDirectories(workspaceDir);
        usersIndex = workspaceDir.resolve("users.ser");
    }

    @Override
    public Path getWorkspaceDir() {
        return workspaceDir;
    }

    /**
     * Retrieves the list of all user usernames from persistent storage.
     * Deserializes users.ser using ObjectInputStream.
     * If the file does not exist, returns an empty list.
     * @return a list of username strings
     * @throws IOException if deserialization or file I/O fails
     */
    @Override
    public List<String> listUsers() throws IOException {
        if (!Files.exists(usersIndex)) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(usersIndex))) {
            Object o = ois.readObject();
            if (o instanceof List) return (List<String>) o;
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    /**
     * Saves the list of all user usernames to persistent storage.
     * Serializes the list to users.ser using ObjectOutputStream.
     * @param users the list of usernames to save
     * @throws IOException if serialization or file I/O fails
     */
    @Override
    public void saveUsersList(List<String> users) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(usersIndex))) {
            oos.writeObject(new ArrayList<>(users));
        }
    }

    /**
     * Loads a user account from persistent storage by username.
     * Deserializes user_<username>.ser using ObjectInputStream.
     * After deserialization, calls postLoadInit() to restore transient fields.
     * @param username the username of the user to load
     * @return the User object with all data restored, or null if not found
     * @throws IOException if deserialization or file I/O fails
     * @throws ClassNotFoundException if the serialized data is incompatible
     */
    @Override
    public User loadUser(String username) throws IOException, ClassNotFoundException {
        Path p = workspaceDir.resolve("user_" + username + ".ser");
        if (!Files.exists(p)) return null;
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(p))) {
            Object o = ois.readObject();
            if (o instanceof User) {
                User u = (User) o;
                u.postLoadInit();
                return u;
            }
            return null;
        }
    }

    /**
     * Saves a user account to persistent storage.
     * Serializes the User object to user_<username>.ser using ObjectOutputStream.
     * The User object includes all albums, photos, captions, and tags.
     * @param user the User object to save
     * @throws IOException if serialization or file I/O fails
     */
    @Override
    public void saveUser(User user) throws IOException {
        Path p = workspaceDir.resolve("user_" + user.getUsername() + ".ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(p))) {
            oos.writeObject(user);
        }
    }
}
