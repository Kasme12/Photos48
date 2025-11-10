package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable {
    private String name;
    private List<Photo> photos;

    public Album(String name) {
        this.name = name;
        this.photos = new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Photo> getPhotos() { return photos; }

    public boolean addPhoto(Photo photo) {
        for (Photo p : photos) {
            if (p.getFilePath().equals(photo.getFilePath())) return false;
        }
        return photos.add(photo);
    }

    public boolean removePhoto(Photo photo) {
        return photos.remove(photo);
    }

    public int getPhotoCount() {
        return photos.size();
    }

    public String getDateRange() {
        if (photos.isEmpty()) return "No photos";
        LocalDateTime earliest = photos.get(0).getDateTaken();
        LocalDateTime latest = earliest;
        for (Photo p : photos) {
            LocalDateTime dt = p.getDateTaken();
            if (dt.isBefore(earliest)) earliest = dt;
            if (dt.isAfter(latest)) latest = dt;
        }
        return earliest.toLocalDate() + " to " + latest.toLocalDate();
    }
}
