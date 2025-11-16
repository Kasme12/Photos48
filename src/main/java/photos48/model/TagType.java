package photos48.model;

import java.io.Serializable;

/**
 * TagType defines a tag type and its multiplicity rule.
 * 
 * - SINGLE: Only one tag of this type per photo; adding a new tag of the same type replaces the old one.
 * - MULTI: Multiple tags of this type are allowed on the same photo.
 * 
 * Examples:
 * - "location" = SINGLE (one location per photo)
 * - "person" = MULTI (multiple people can be tagged)
 * 
 * @author Esmeralda Bencosme
 * @version 1.0
 */
public class TagType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Multiplicity rule for tags.
     */
    public enum Multiplicity { 
        /** Only one tag of this type per photo */
        SINGLE, 
        /** Multiple tags of this type allowed per photo */
        MULTI 
    }

    private String name;
    private Multiplicity multiplicity;

    /**
     * Creates a TagType.
     * @param name the tag type name (e.g., "location", "person")
     * @param multiplicity the multiplicity rule (SINGLE or MULTI)
     */
    public TagType(String name, Multiplicity multiplicity) {
        this.name = name;
        this.multiplicity = multiplicity;
    }

    /**
     * Gets the tag type name.
     * @return name
     */
    public String getName() { return name; }

    /**
     * Gets the multiplicity rule.
     * @return Multiplicity.SINGLE or Multiplicity.MULTI
     */
    public Multiplicity getMultiplicity() { return multiplicity; }

    /**
     * Returns a string representation.
     * @return "name(SINGLE)" or "name(MULTI)"
     */
    @Override
    public String toString() { return name + "(" + multiplicity + ")"; }
}
