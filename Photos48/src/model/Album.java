package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Represents a photo album containing multiple photos.
 * Each album has a name and a list of photos.
 */
public class Album implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private ArrayList<Photo> photos;

    public Album(String name) {
        this.name = name;
        this.photos = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public boolean addPhoto(Photo photo) {
        if (!photos.contains(photo)) {
            photos.add(photo);
            return true;
        }
        return false;
    }

    public boolean removePhoto(Photo photo) {
        return photos.remove(photo);
    }

    public boolean containsPhoto(Photo photo) {
        return photos.contains(photo);
    }

    public LocalDateTime getEarliestDate() {
        return photos.stream()
                     .map(Photo::getDateTaken)
                     .min(Comparator.naturalOrder())
                     .orElse(null);
    }

    public LocalDateTime getLatestDate() {
        return photos.stream()
                     .map(Photo::getDateTaken)
                     .max(Comparator.naturalOrder())
                     .orElse(null);
    }
}
