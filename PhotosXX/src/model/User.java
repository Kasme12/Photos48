package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String username;
    private List<Album> albums;

    public User(String username) {
        this.username = username;
        this.albums = new ArrayList<>();
    }

    public String getUsername() { return username; }
    public List<Album> getAlbums() { return albums; }

    public boolean addAlbum(String name) {
        for (Album a : albums) {
            if (a.getName().equalsIgnoreCase(name)) return false;
        }
        return albums.add(new Album(name));
    }

    public boolean deleteAlbum(String name) {
        return albums.removeIf(a -> a.getName().equalsIgnoreCase(name));
    }

    public Album getAlbum(String name) {
        for (Album a : albums) {
            if (a.getName().equalsIgnoreCase(name)) return a;
        }
        return null;
    }
}
