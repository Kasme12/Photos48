package photos48.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Album stores photo IDs (UUID) and computes derived values like photo count and date range.
 * Album names are unique per user but may duplicate across users.
 * 
 * @author Esmeralda Bencosme
 * @version 1.0
 */
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private List<UUID> photoIds = new ArrayList<>();

    /**
     * Creates an album with the given name.
     * @param name the album name (must be unique per user)
     */
    public Album(String name) { this.name = name; }

    /**
     * Gets the album name.
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Renames the album.
     * @param newName the new album name
     */
    public void setName(String newName) { this.name = newName; }
    /**
     * Gets the unmodifiable list of photo UUIDs in this album.
     * @return immutable list of photo IDs
     */
    public List<UUID> getPhotoIds() { return Collections.unmodifiableList(photoIds); }

    /**
     * Adds a photo to this album (idempotent).
     * @param id the photo UUID
     */
    public void addPhoto(UUID id) {
        if (!photoIds.contains(id)) photoIds.add(id);
    }

    /**
     * Removes a photo from this album.
     * @param id the photo UUID
     */
    public void removePhoto(UUID id) { photoIds.remove(id); }

    /**
     * Gets the number of photos in this album.
     * @return photo count
     */
    public int getPhotoCount() { return photoIds.size(); }

    /**
     * Computes the earliest photo date in this album.
     * @param photoStore map of UUID to Photo objects
     * @return earliest LocalDateTime, or null if no photos
     */
    public LocalDateTime getEarliest(java.util.Map<UUID, Photo> photoStore) {
        LocalDateTime earliest = null;
        for (UUID id : photoIds) {
            Photo p = photoStore.get(id);
            if (p == null) continue;
            LocalDateTime dt = p.getDateTime();
            if (earliest == null || dt.isBefore(earliest)) earliest = dt;
        }
        return earliest;
    }

    /**
     * Computes the latest photo date in this album.
     * @param photoStore map of UUID to Photo objects
     * @return latest LocalDateTime, or null if no photos
     */
    public LocalDateTime getLatest(java.util.Map<UUID, Photo> photoStore) {
        LocalDateTime latest = null;
        for (UUID id : photoIds) {
            Photo p = photoStore.get(id);
            if (p == null) continue;
            LocalDateTime dt = p.getDateTime();
            if (latest == null || dt.isAfter(latest)) latest = dt;
        }
        return latest;
    }
}
