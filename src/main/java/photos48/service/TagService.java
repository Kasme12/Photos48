package photos48.service;

import photos48.model.Photo;
import photos48.model.Tag;
import photos48.model.TagType;
import photos48.model.User;

import java.util.Iterator;

/**
 * TagService manages tag operations and enforces tag multiplicity rules.
 * 
 * Each tag has a type (e.g., "location", "person") and value (e.g., "Paris", "Alice").
 * The multiplicity of a tag type determines how many tags of that type can be assigned
 * to a single photo:
 * - SINGLE multiplicity: Only one tag of this type per photo (e.g., location)
 * - MULTI multiplicity: Multiple tags of this type per photo (e.g., person)
 *
 * When adding a tag with SINGLE multiplicity, TagService automatically removes any
 * existing tags of the same type before adding the new one. This ensures the multiplicity
 * rule is enforced.
 *
 * TagService lazily creates TagType entries if they don't exist (defaulting to MULTI).
 * 
 * @author Esmeralda Bencosme
 * @author Armaan Sleem
 * @version 1.0
 */
public class TagService {

    /** Reference to the user whose tags are being managed */
    private final User user;

    /**
     * Constructs a TagService for the given user.
     * @param user the User whose tags will be managed
     */
    public TagService(User user) {
        this.user = user;
    }

    /**
     * Retrieves the user associated with this TagService.
     * @return the User object
     */
    public User getUser() { return user; }

    /**
     * Adds a tag to a photo, enforcing multiplicity rules.
     * 
     * If the tag type doesn't exist, it is created with MULTI multiplicity.
     * If the tag type has SINGLE multiplicity, any existing tags of the same type
     * are removed before adding the new tag.
     * If the tag type has MULTI multiplicity, the tag is added without removing others.
     * 
     * @param p the Photo to tag
     * @param t the Tag to add (must have type and value)
     */
    public void addTag(Photo p, Tag t) {
        TagType tt = user.getTagTypes().get(t.getType());
        if (tt == null) user.getTagTypes().put(t.getType(), new TagType(t.getType(), TagType.Multiplicity.MULTI));
        tt = user.getTagTypes().get(t.getType());
        if (tt.getMultiplicity() == TagType.Multiplicity.SINGLE) {
            // remove any existing same-type tags
            Iterator<Tag> it = p.getTags().iterator();
            while (it.hasNext()) {
                Tag existing = it.next();
                if (existing.getType().equals(t.getType())) it.remove();
            }
            p.addTag(t);
        } else {
            p.addTag(t);
        }
    }

    /**
     * Removes a tag from a photo.
     * @param p the Photo to remove the tag from
     * @param t the Tag to remove
     */
    public void removeTag(Photo p, Tag t) { p.removeTag(t); }
}

