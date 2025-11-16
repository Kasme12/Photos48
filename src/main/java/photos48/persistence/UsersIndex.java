package photos48.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lightweight wrapper to track usernames (delegates to DataStore implementations).
 * 
 * @author Esmeralda Bencosme
 * @version 1.0
 */
public class UsersIndex {
    private final DataStore ds;

    public UsersIndex(DataStore ds) { this.ds = ds; }

    public List<String> list() {
        try { return ds.listUsers(); }
        catch (IOException e) { return new ArrayList<>(); }
    }

    public void save(List<String> users) {
        try { ds.saveUsersList(users); }
        catch (IOException e) { /* swallow for now; caller shows dialog */ }
    }
}
