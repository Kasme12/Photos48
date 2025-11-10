package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Admin implements Serializable {
    private List<User> users;

    public Admin() {
        this.users = new ArrayList<>();
    }

    public List<User> getUsers() { return users; }

    public boolean addUser(String username) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) return false;
        }
        users.add(new User(username));
        return true;
    }

    public boolean deleteUser(String username) {
        return users.removeIf(u -> u.getUsername().equalsIgnoreCase(username));
    }

    public User getUser(String username) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) return u;
        }
        return null;
    }
}
