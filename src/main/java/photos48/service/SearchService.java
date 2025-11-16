package photos48.service;

import photos48.model.Photo;
import photos48.model.Tag;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SearchService provides flexible search capabilities for photos.
 * 
 * Supports multiple search modes:
 * - Date range search: Find photos within a date/time range (inclusive)
 * - Single tag search: Find photos with a specific tag (type and value)
 * - AND search: Find photos with both tag A and tag B
 * - OR search: Find photos with either tag A or tag B
 *
 * All searches operate on a user's canonical photo store (Map<UUID, Photo>).
 * Search results are returned as lists of Photo objects and can be used to:
 * - Display search results in the UI
 * - Create new albums from search results
 * - Perform further analysis or operations
 * 
 * @author Esmeralda Bencosme
 * @version 1.0
 */
public class SearchService {

    /** The canonical photo store to search in */
    private final Map<UUID, Photo> photoStore;

    /**
     * Constructs a SearchService for the given photo store.
     * @param photoStore the Map<UUID, Photo> to search in
     */
    public SearchService(Map<UUID, Photo> photoStore) { this.photoStore = photoStore; }

    /**
     * Searches for photos within a date/time range.
     * Returns all photos whose dateTime falls within [from, to] inclusive.
     * @param from the start of the date range (inclusive)
     * @param to the end of the date range (inclusive)
     * @return a list of photos within the range (empty list if none found)
     */
    public List<Photo> byDateRange(LocalDateTime from, LocalDateTime to) {
        return photoStore.values().stream()
                .filter(p -> !p.getDateTime().isBefore(from) && !p.getDateTime().isAfter(to))
                .collect(Collectors.toList());
    }

    /**
     * Searches for photos with a specific tag.
     * @param type the tag type (e.g., "location", "person")
     * @param value the tag value (e.g., "Paris", "Alice")
     * @return a list of photos with this tag (empty list if none found)
     */
    public List<Photo> bySingleTag(String type, String value) {
        String t = type == null ? "" : type.trim().toLowerCase();
        String v = value == null ? "" : value.trim().toLowerCase();
        return photoStore.values().stream()
            .filter(p -> p.getTags().stream().anyMatch(tag ->
                tag.getType() != null && tag.getValue() != null &&
                tag.getType().trim().toLowerCase().equals(t) &&
                tag.getValue().trim().toLowerCase().equals(v)
            ))
            .collect(Collectors.toList());
    }

    /**
     * Searches for photos with BOTH tag A and tag B (AND logic).
     * @param a the first tag to search for
     * @param b the second tag to search for
     * @return a list of photos that have both tags (empty list if none found)
     */
    public List<Photo> byAnd(Tag a, Tag b) {
        String ta = a.getType() == null ? "" : a.getType().trim().toLowerCase();
        String va = a.getValue() == null ? "" : a.getValue().trim().toLowerCase();
        String tb = b.getType() == null ? "" : b.getType().trim().toLowerCase();
        String vb = b.getValue() == null ? "" : b.getValue().trim().toLowerCase();
        return photoStore.values().stream()
                .filter(p -> {
                    boolean hasa = p.getTags().stream().anyMatch(tag -> tag.getType()!=null && tag.getValue()!=null && tag.getType().trim().toLowerCase().equals(ta) && tag.getValue().trim().toLowerCase().equals(va));
                    boolean hasb = p.getTags().stream().anyMatch(tag -> tag.getType()!=null && tag.getValue()!=null && tag.getType().trim().toLowerCase().equals(tb) && tag.getValue().trim().toLowerCase().equals(vb));
                    return hasa && hasb;
                })
                .collect(Collectors.toList());
    }

    /**
     * Searches for photos with EITHER tag A or tag B (OR logic).
     * @param a the first tag to search for
     * @param b the second tag to search for
     * @return a list of photos that have at least one of the tags (empty list if none found)
     */
    public List<Photo> byOr(Tag a, Tag b) {
        String ta = a.getType() == null ? "" : a.getType().trim().toLowerCase();
        String va = a.getValue() == null ? "" : a.getValue().trim().toLowerCase();
        String tb = b.getType() == null ? "" : b.getType().trim().toLowerCase();
        String vb = b.getValue() == null ? "" : b.getValue().trim().toLowerCase();
        return photoStore.values().stream()
            .filter(p -> p.getTags().stream().anyMatch(tag -> tag.getType()!=null && tag.getValue()!=null && (tag.getType().trim().toLowerCase().equals(ta) && tag.getValue().trim().toLowerCase().equals(va)))
                || p.getTags().stream().anyMatch(tag -> tag.getType()!=null && tag.getValue()!=null && (tag.getType().trim().toLowerCase().equals(tb) && tag.getValue().trim().toLowerCase().equals(vb))))
            .collect(Collectors.toList());
    }
}
