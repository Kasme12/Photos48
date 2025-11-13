package model;

import java.io.*;
import java.util.HashMap;

/**
 * Handles saving and loading user data to disk using Java serialization.
 */
public class UserDataStore {
    private static final String DATA_FILE = "data/users.dat";

    public static void saveUsers(HashMap<String, User> users) throws IOException {
        File file = new File(DATA_FILE);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(users);
        }
    }

    public static HashMap<String, User> loadUsers() throws IOException, ClassNotFoundException {
        File file = new File(DATA_FILE);
        if (!file.exists()) return new HashMap<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (HashMap<String, User>) in.readObject();
        }
    }
}
