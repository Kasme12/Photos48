package photos48.model;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Photo model represents a single photo in the system.
 * Each photo is uniquely identified by a UUID and stores:
 * - path: absolute filesystem path (for user imports) or relative path (for stock photos)
 * - caption: user-provided description
 * - dateTime: extracted from file last-modified time, truncated to seconds precision
 * - tags: user-added tags with type and value
 * 
 * Photo edits (caption, tags) are reflected everywhere the photo appears across albums.
 * 
 * @author Esmeralda Bencosme
 * @author Armaan Sleem
 * @version 1.0
 */
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String path; // absolute or repo-relative (stock)
    private String caption;
    private LocalDateTime dateTime; // truncated to seconds
    private Set<Tag> tags = new HashSet<>();

    /**
     * Creates a Photo by reading the file's last-modified time.
     * The dateTime is automatically computed and truncated to seconds precision.
     * 
     * @param path absolute or relative filesystem path to the image file
     * @param caption optional caption (can be empty)
     */
    public Photo(String path, String caption) {
        this.id = UUID.randomUUID();
        this.path = path;
        this.caption = caption;
        this.dateTime = readFileTimeTruncated(path);
    }

    /**
     * Gets the photo's unique identifier.
     * @return UUID
     */
    public UUID getId() { return id; }

    /**
     * Gets the file path (absolute for user imports, relative for stock).
     * @return path string
     */
    public String getPath() { return path; }

    /**
     * Gets the photo's caption.
     * @return caption or empty string
     */
    public String getCaption() { return caption; }

    /**
     * Updates the photo's caption. This change is reflected everywhere the photo appears.
     * @param caption the new caption
     */
    public void setCaption(String caption) { this.caption = caption; }

    /**
     * Gets the date/time this photo was taken (from file last-modified, seconds precision).
     * @return LocalDateTime truncated to seconds (nanos=0)
     */
    public LocalDateTime getDateTime() { return dateTime; }

    /**
     * Gets all tags on this photo.
     * @return unmodifiable set of Tag objects
     */
    public Set<Tag> getTags() { return tags; }

    /**
     * Adds a tag to this photo (enforces uniqueness by (type, value) pair).
     * @param t the Tag object
     */
    public void addTag(Tag t) { tags.add(t); }

    /**
     * Removes a tag from this photo.
     * @param t the Tag object
     */
    public void removeTag(Tag t) { tags.remove(t); }

    /**
     * Reads the file's last-modified time and truncates to seconds precision.
     * If the file doesn't exist, returns the current time (also truncated).
     * 
     * @param pathStr file path (absolute or relative)
     * @return LocalDateTime with nanos=0 (seconds precision)
     */
    private static LocalDateTime readFileTimeTruncated(String pathStr) {
        try {
            Path p = Path.of(pathStr);
            if (!Files.exists(p)) return LocalDateTime.now().withNano(0);
            Instant instant = Files.getLastModifiedTime(p).toInstant();
            LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            return ldt.withNano(0);
        } catch (Exception e) {
            return LocalDateTime.now().withNano(0);
        }
    }
}
