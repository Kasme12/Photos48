package model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a photo stored on disk. Controllers expect a simple POJO
 * with file path, caption, date taken and a list of tags.
 */
public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String filePath;
    private String caption;
    private LocalDateTime dateTaken;
    private List<Tag> tags;

    public Photo(String filePath) {
        this.filePath = filePath;
        this.caption = "";
        this.tags = new ArrayList<>();
        // try to set dateTaken from file last-modified time, fallback to now
        try {
            Path p = new File(filePath).toPath();
            if (Files.exists(p)) {
                Instant instant = Files.getLastModifiedTime(p).toInstant();
                this.dateTaken = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            } else {
                this.dateTaken = LocalDateTime.now();
            }
        } catch (IOException e) {
            this.dateTaken = LocalDateTime.now();
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption == null ? "" : caption;
    }

    public LocalDateTime getDateTaken() {
        return dateTaken;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public boolean addTag(Tag tag) {
        if (tag == null) return false;
        if (!tags.contains(tag)) {
            tags.add(tag);
            return true;
        }
        return false;
    }

    public boolean removeTag(Tag tag) {
        return tags.remove(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;
        Photo photo = (Photo) o;
        return Objects.equals(filePath, photo.filePath);
    }

    @Override
    public int hashCode() {
        return filePath == null ? 0 : filePath.hashCode();
    }

    @Override
    public String toString() {
        return "Photo{" + filePath + "}";
    }
}
