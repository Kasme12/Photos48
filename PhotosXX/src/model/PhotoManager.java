package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoManager {
    private static List<User> users = new ArrayList<>();

    public static void loadData() {
        try {
            users = DataStore.load();
        } catch (Exception e) {
            users = new ArrayList<>();
        }
    }

    public static void saveData() {
        try {
            DataStore.save(users);
        } catch (IOException e) {
            e.printStackTrace(); // Replace with GUI alert in controller
        }
    }

    public static List<User> getUsers() { return users; }

    public static User getUser(String username) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) return u;
        }
        return null;
    }

    public static boolean addUser(String username) {
        if (getUser(username) != null) return false;
        users.add(new User(username));
        return true;
    }

    public static boolean deleteUser(String username) {
        return users.removeIf(u -> u.getUsername().equalsIgnoreCase(username));
    }
}
