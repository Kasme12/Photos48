package photos48.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * User represents a user account in the system.
 * Each user has:
 * - username (unique across the system)
 * - albums map (album name -> Album object)
 * - photoStore map (photo UUID -> Photo object, canonical storage per-user)
 * - tagTypes map (tag type name -> TagType, defines multiplicity rules)
 * 
 * All photos in a user's album references are stored in photoStore.
 * Tags on photos are enforced by TagType multiplicity rules (SINGLE or MULTI).
 * 
 * @author Esmeralda Bencosme
 * @version 1.0
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private Map<String, Album> albums = new LinkedHashMap<>();
    private Map<UUID, Photo> photoStore = new HashMap<>();
    private Map<String, TagType> tagTypes = new HashMap<>();
    private boolean copyOnImport = true;

    /**
     * Creates a new User.
     * @param username the unique username for this account
     */
    public User(String username) { this.username = username; }

    /**
     * Returns whether imported photos should be copied into the application workspace.
     * Default is true.
     * @return copy-on-import flag
     */
    public boolean isCopyOnImport() { return copyOnImport; }

    /**
     * Sets the copy-on-import flag for this user.
     * @param copyOnImport true to copy imported files into workspace
     */
    public void setCopyOnImport(boolean copyOnImport) { this.copyOnImport = copyOnImport; }

    /**
     * Gets the username.
     * @return username
     */
    public String getUsername() { return username; }

    /**
     * Gets the user's albums map (album name -> Album).
     * @return map of albums
     */
    public Map<String, Album> getAlbums() { return albums; }

    /**
     * Gets the canonical photo store (UUID -> Photo).
     * All photos in this user's albums are stored here.
     * @return map of photos
     */
    public Map<UUID, Photo> getPhotoStore() { return photoStore; }

    /**
     * Gets the tag type definitions for this user (type name -> TagType).
     * Controls multiplicity rules (SINGLE vs MULTI) for each tag type.
     * @return map of tag types
     */
    public Map<String, TagType> getTagTypes() { return tagTypes; }

    /**
     * Adds an album to this user.
     * @param a the Album object
     */
    public void addAlbum(Album a) { albums.put(a.getName(), a); }

    /**
     * Removes an album by name.
     * @param name the album name
     */
    public void removeAlbum(String name) { albums.remove(name); }

    /**
     * Initializes default tag types after deserialization.
     * Ensures "location" (SINGLE) and "person" (MULTI) types are always defined.
     * Called automatically by ObjectDataStore.loadUser().
     */
    public void postLoadInit() {
        if (tagTypes.isEmpty()) {
            tagTypes.put("location", new TagType("location", TagType.Multiplicity.SINGLE));
            tagTypes.put("person", new TagType("person", TagType.Multiplicity.MULTI));
        }
    }
}
