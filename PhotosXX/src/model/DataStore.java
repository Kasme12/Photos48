
public class DataStore {
    public static void saveUsers(List<User> users) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data/users.dat"))) {
            out.writeObject(users);
        }
    }

    public static List<User> loadUsers() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("data/users.dat"))) {
            return (List<User>) in.readObject();
        }
    }
}
