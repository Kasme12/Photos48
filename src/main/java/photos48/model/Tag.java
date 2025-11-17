package photos48.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Tag represents a (type, value) pair attached to a photo.
 * Tags are unique per photo by (type, value) combination.
 * Multiplicity rules (SINGLE vs MULTI) are enforced per TagType, not here.
 * 
 * @author Esmeralda Bencosme
 * @author Armaan Sleem
 * @version 1.0
 */
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;
    private String value;

    /**
     * Creates a Tag with a type and value.
     * @param type the tag type (e.g., "location", "person")
     * @param value the tag value (e.g., "Beach", "John")
     */
    public Tag(String type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Gets the tag type.
     * @return type
     */
    public String getType() { return type; }

    /**
     * Gets the tag value.
     * @return value
     */
    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return Objects.equals(type, tag.type) && Objects.equals(value, tag.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public String toString() { return type + ":" + value; }
}
