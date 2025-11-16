package photos48.service;

import photos48.model.Photo;
import photos48.model.User;

import java.util.UUID;

/**
 * PhotoService provides operations for managing individual Photo objects.
 * Handles adding/removing photos to/from a user's canonical photo store, updating photo
 * metadata (captions), and retrieving photo information.
 *
 * PhotoService works with the user's canonical photo storage (photoStore), which maintains
 * all Photo objects keyed by their unique ID (UUID). When a photo is modified through this
 * service, the change is reflected in all albums that reference that photo, since albums
 * store photo references (by UUID), not copies.
 *
 * Note: Photo caption updates are reflected automatically across all albums containing
 * the photo, as they all reference the same Photo object in the user's photoStore.
 */
public class PhotoService {

    /** Reference to the user whose photos are being managed */
    private final User user;

    /**
     * Constructs a PhotoService for the given user.
     * @param user the User whose photos will be managed
     */
    public PhotoService(User user) {
        this.user = user;
    }

    /**
     * Adds a new photo to the user's canonical photo store.
     * The photo is stored by its UUID ID and becomes available for addition to albums.
     * @param photo the Photo to add
     */
    public void addPhoto(Photo photo) {
        user.getPhotoStore().put(photo.getId(), photo);
    }

    /**
     * Removes a photo from the user's canonical photo store.
     * The photo is removed by its UUID ID. Note that album references to this photo
     * should be cleaned up separately to maintain consistency.
     * @param photoId the UUID of the photo to remove
     */
    public void removePhoto(UUID photoId) {
        user.getPhotoStore().remove(photoId);
    }

    /**
     * Removes the photo from the user's canonical store if it is no longer referenced
     * by any album. If the photo file resides under the application's workspace
     * user photos directory, the file is deleted as well.
     * @param photoId the UUID of the photo to consider for removal
     */
    public void removePhotoIfOrphan(UUID photoId) {
        // check all albums for references
        boolean referenced = user.getAlbums().values().stream().anyMatch(a -> a.getPhotoIds().contains(photoId));
        if (!referenced) {
            Photo p = user.getPhotoStore().remove(photoId);
            if (p != null) {
                try {
                    java.nio.file.Path workspace = photos48.ui.SceneManager.getDataStore().getWorkspaceDir();
                    java.nio.file.Path userPhotos = workspace.resolve("user_" + user.getUsername()).resolve("photos");
                    java.nio.file.Path photoPath = java.nio.file.Path.of(p.getPath());
                    if (photoPath.startsWith(userPhotos) && java.nio.file.Files.exists(photoPath)) {
                        java.nio.file.Files.delete(photoPath);
                    }
                } catch (Exception e) {
                    // ignore deletion errors; file may be external or in use
                }
            }
        }
    }

    /**
     * If the photo is not referenced by any album, returns the filesystem Path of the
     * photo file if it resides under the user's workspace photos directory; otherwise
     * returns null. This method does NOT remove the photo from the store or delete files.
     * @param photoId the photo UUID to check
     * @return Path to the orphan file under user photos directory, or null if none
     */
    public java.nio.file.Path findOrphanFile(UUID photoId) {
        boolean referenced = user.getAlbums().values().stream().anyMatch(a -> a.getPhotoIds().contains(photoId));
        if (referenced) return null;
        Photo p = user.getPhotoStore().get(photoId);
        if (p == null) return null;
        try {
            java.nio.file.Path workspace = photos48.ui.SceneManager.getDataStore().getWorkspaceDir();
            java.nio.file.Path userPhotos = workspace.resolve("user_" + user.getUsername()).resolve("photos");
            java.nio.file.Path photoPath = java.nio.file.Path.of(p.getPath());
            if (photoPath.startsWith(userPhotos) && java.nio.file.Files.exists(photoPath)) {
                return photoPath;
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    /**
     * Removes the photo object from the user's canonical photo store without touching files.
     * @param photoId the UUID to remove
     * @return true if removed, false otherwise
     */
    public boolean removePhotoFromStore(UUID photoId) {
        return user.getPhotoStore().remove(photoId) != null;
    }

    /**
     * Updates the caption/description of a photo.
     * The change is immediately reflected in all albums containing this photo.
     * @param photoId the UUID of the photo to update
     * @param caption the new caption text (can be empty string for no caption)
     */
    public void updatePhotoCaption(UUID photoId, String caption) {
        Photo p = user.getPhotoStore().get(photoId);
        if (p != null) p.setCaption(caption);
    }

    /**
     * Retrieves a photo by its UUID.
     * @param photoId the UUID of the photo to retrieve
     * @return the Photo object, or null if not found
     */
    public Photo getPhoto(UUID photoId) {
        return user.getPhotoStore().get(photoId);
    }
}
