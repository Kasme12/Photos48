package model;

import java.io.*;
import java.util.List;

public class DataStore {
    private static final String DATA_FILE = "data/users.dat";

    public static void save(List<User> users) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(users);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<User> load() throws IOException, ClassNotFoundException {
        File file = new File(DATA_FILE);
        if (!file.exists()) return new java.util.ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (List<User>) in.readObject();
        }
    }
}
