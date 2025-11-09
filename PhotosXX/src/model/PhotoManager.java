public class PhotoManager {
    private static List<User> users;
    private static Admin admin;

    public static void loadData();
    public static void saveData();
    public static User getUser(String username);
    public static void addUser(String username);
    public static void deleteUser(String username);
}
