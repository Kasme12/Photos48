package photos48.ui;

import photos48.persistence.DataStore;

/**
 * Simple scene manager to hold references to the DataStore.
 */
public class SceneManager {
    private static DataStore ds;

    public static void init(DataStore dataStore) {
        ds = dataStore;
    }

    public static DataStore getDataStore() {
        return ds;
    }
}
