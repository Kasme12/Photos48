package photos48.service;

import photos48.model.Album;
import photos48.model.Photo;
import photos48.model.User;
import photos48.persistence.DataStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.List;

/**
 * StockService initializes a "stock" user with sample photos on application first launch.
 * 
 * The stock user is created with one album ("stock") containing sample images from the
 * ./data/ directory. This provides users with an immediate example of the application
 * functionality without requiring them to import their own photos first.
 *
 * StockService gracefully handles missing image files or directory:
 * - If ./data/stock/ does not exist, a stock user is created with an empty album
 * - If image files are not found, only the user and album are created (no photos added)
 *
 * initStockIfNeeded() is called once at application startup. If a "stock" user already
 * exists, the service does nothing.
 */
public class StockService {

    /** Reference to the DataStore for persistence operations */
    private final DataStore ds;

    /**
     * Constructs a StockService with the given DataStore.
     * @param ds the DataStore to use for persistence operations
     */
    public StockService(DataStore ds) {
        this.ds = ds;
    }

    /**
     * Initializes the stock user and album if they do not already exist.
     * 
    * Creates a "stock" user with a "stock" album containing sample images from
    * ./data/. Supported image formats: jpg, jpeg, png, bmp, gif.
     *
     * If the stock user already exists (checked by username), this method does nothing.
     * If image files are missing or the directory doesn't exist, the stock user and
     * album are still created, but with no photos (graceful degradation).
     *
     * This method should be called once at application startup.
     * Any IOException during initialization is caught and silently ignored.
     */
    public void initStockIfNeeded() {
        try {
            List<String> users = ds.listUsers();
            if (!users.contains("stock")) {
                // Create stock user and album
                User stockUser = new User("stock");
                Album stockAlbum = new Album("stock");
                stockUser.addAlbum(stockAlbum);

                // Scan ./data/ for image files and add them
                Path stockDir = Path.of("data");
                if (!Files.exists(stockDir)) {
                    Files.createDirectories(stockDir);
                }

                // If no image files are present, generate placeholder PNGs so stock has images
                boolean hasImages = false;
                try (var stream = Files.list(stockDir)) {
                    hasImages = stream.anyMatch(p -> isImageFile(p.getFileName().toString()));
                }

                if (!hasImages) {
                    // generate 5 simple colored PNG placeholders in ./data
                    for (int i = 1; i <= 5; i++) {
                        BufferedImage img = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB);
                        Graphics2D g = img.createGraphics();
                        // simple distinct background colors
                        Color bg = switch (i) {
                            case 1 -> new Color(200, 120, 120);
                            case 2 -> new Color(120, 200, 140);
                            case 3 -> new Color(120, 160, 220);
                            case 4 -> new Color(220, 200, 120);
                            default -> new Color(180, 140, 200);
                        };
                        g.setColor(bg);
                        g.fillRect(0, 0, img.getWidth(), img.getHeight());
                        g.dispose();
                        Path out = stockDir.resolve("stock" + i + ".png");
                        try {
                            ImageIO.write(img, "png", out.toFile());
                        } catch (Exception e) {
                            // ignore write failures for placeholder generation
                        }
                    }
                }

                Files.list(stockDir)
                        .filter(p -> isImageFile(p.getFileName().toString()))
                        .forEach(p -> {
                            Photo photo = new Photo(p.toString(), p.getFileName().toString());
                            stockUser.getPhotoStore().put(photo.getId(), photo);
                            stockAlbum.addPhoto(photo.getId());
                        });

                // Persist stock user
                ds.saveUser(stockUser);
                users.add("stock");
                ds.saveUsersList(users);
            }
        } catch (IOException e) {
            // silently fail; stock can be recreated manually later
        }
    }

    /**
     * Checks if a filename represents a supported image file.
     * Supported formats: jpg, jpeg, png, bmp, gif (case-insensitive).
     * @param filename the filename to check
     * @return true if the filename ends with a supported image extension
     */
    private static boolean isImageFile(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                || lower.endsWith(".png") || lower.endsWith(".bmp")
                || lower.endsWith(".gif");
    }
}
