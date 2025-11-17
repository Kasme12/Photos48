package photos48.service;

import photos48.model.User;
import photos48.persistence.DataStore;

import java.io.IOException;
import java.util.List;

/**
 * AuthService handles login logic. Username only.
 * @author Esmeralda Bencosme
 * @author Armaan Sleem
 * @version 1.0
 */
public class AuthService {
    private final DataStore ds;

    public AuthService(DataStore ds) { this.ds = ds; }

    public boolean userExists(String username) {
        try { return ds.listUsers().contains(username); }
        catch (IOException e) { return false; }
    }

    public User login(String username) throws IOException, ClassNotFoundException {
        if ("admin".equals(username)) return new User("admin");
        User u = ds.loadUser(username);
        return u;
    }

    public void register(String username) throws IOException {
        List<String> users = ds.listUsers();
        if (!users.contains(username)) {
            users.add(username);
            ds.saveUsersList(users);
        }
    }
}
