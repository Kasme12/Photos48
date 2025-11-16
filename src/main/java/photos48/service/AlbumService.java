package photos48.service;

import photos48.model.Album;
import photos48.model.Photo;
import photos48.model.User;

import java.util.UUID;

/**
 * AlbumService handles album CRUD operations and photo management within albums.
 * Operations: create album, delete album, rename album, add/remove/copy/move photos.
 * 
 * Note: Album names must be unique per user but can duplicate across users.
 * All photos across albums are stored in the User's canonical photoStore.
 * 
 * @author Esmeralda Bencosme
 * @version 1.0
 */
public class AlbumService {

    private final User user;

    /**
     * Creates an AlbumService for a user.
     * @param user the User object
     */
    public AlbumService(User user) {
        this.user = user;
    }

    /**
     * Creates an album if it doesn't already exist.
     * @param name the album name (must be unique per user)
     */
    public void createAlbum(String name) {
        if (!user.getAlbums().containsKey(name)) {
            user.addAlbum(new Album(name));
        }
    }

    /**
     * Deletes an album.
     * @param name the album name
     */
    public void deleteAlbum(String name) {
        user.removeAlbum(name);
    }

    /**
     * Renames an album.
     * @param oldName the current album name
     * @param newName the new album name
     * @throws IllegalArgumentException if newName already exists
     */
    public void renameAlbum(String oldName, String newName) {
        if (user.getAlbums().containsKey(newName) && !oldName.equals(newName)) {
            throw new IllegalArgumentException("Album '" + newName + "' already exists");
        }
        Album album = user.getAlbums().remove(oldName);
        if (album != null) {
            album.setName(newName);
            user.addAlbum(album);
        }
    }

    /**
     * Adds a photo to an album.
     * @param albumName the album name
     * @param photoId the photo UUID
     */
    public void addPhotoToAlbum(String albumName, UUID photoId) {
        Album album = user.getAlbums().get(albumName);
        if (album != null) {
            album.addPhoto(photoId);
        }
    }

    /**
     * Removes a photo from an album.
     * Note: The photo remains in the user's photoStore; it's only removed from this album.
     * @param albumName the album name
     * @param photoId the photo UUID
     */
    public void removePhotoFromAlbum(String albumName, UUID photoId) {
        Album album = user.getAlbums().get(albumName);
        if (album != null) {
            album.removePhoto(photoId);
        }
    }

    /**
     * Copies a photo reference to another album.
     * The photo appears in both albums and remains a single Photo object in photoStore.
     * @param fromAlbumName the source album (not modified)
     * @param toAlbumName the destination album
     * @param photoId the photo UUID
     */
    public void copyPhotoAcrossAlbums(String fromAlbumName, String toAlbumName, UUID photoId) {
        Album toAlbum = user.getAlbums().get(toAlbumName);
        if (toAlbum != null) {
            toAlbum.addPhoto(photoId);
        }
    }

    /**
     * Moves a photo from one album to another.
     * The photo reference is removed from the source album and added to the destination.
     * @param fromAlbumName the source album
     * @param toAlbumName the destination album
     * @param photoId the photo UUID
     */
    public void movePhotoAcrossAlbums(String fromAlbumName, String toAlbumName, UUID photoId) {
        removePhotoFromAlbum(fromAlbumName, photoId);
        addPhotoToAlbum(toAlbumName, photoId);
    }
}
