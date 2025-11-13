package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a user with a collection of albums.
 * Each user has a unique username and manages their own photo albums.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private ArrayList<Album> albums;

    public User(String username) {
        this.username = username;
        this.albums = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public boolean addAlbum(String name) {
        if (getAlbum(name) == null) {
            albums.add(new Album(name));
            return true;
        }
        return false;
    }

    public boolean deleteAlbum(String name) {
        Album album = getAlbum(name);
        return album != null && albums.remove(album);
    }

    public boolean renameAlbum(String oldName, String newName) {
        Album album = getAlbum(oldName);
        if (album != null && getAlbum(newName) == null) {
            album.setName(newName);
            return true;
        }
        return false;
    }

    public Album getAlbum(String name) {
        return albums.stream()
                     .filter(a -> a.getName().equalsIgnoreCase(name))
                     .findFirst()
                     .orElse(null);
    }
}
