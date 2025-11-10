package model;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class Photo implements Serializable {
    private String filePath;
    private String caption;
    private LocalDateTime dateTaken;
    private List<Tag> tags;

    public Photo(String filePath) {
        this.filePath = filePath;
        this.caption = "";
        this.tags = new ArrayList<>();
        File file = new File(filePath);
        this.dateTaken = LocalDateTime.ofInstant(
                file.lastModified() > 0 ?
                        java.time.Instant.ofEpochMilli(file.lastModified()) :
                        java.time.Instant.now(),
                ZoneId.systemDefault()
        );
    }

    public String getFilePath() { return filePath; }
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    public LocalDateTime getDateTaken() { return dateTaken; }

    public List<Tag> getTags() { return tags; }
    public void addTag(Tag tag) {
        if (!tags.contains(tag)) tags.add(tag);
    }
    public void removeTag(Tag tag) {
        tags.remove(tag);
    }
}
