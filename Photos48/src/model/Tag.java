package model;

import java.io.Serializable;

/**
 * Represents a tag with a name and value, such as ("location", "Paris").
 * Tags are used for searching and grouping photos.
 */
public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String value;

    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tag) {
            Tag other = (Tag) obj;
            return name.equalsIgnoreCase(other.name) && value.equalsIgnoreCase(other.value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode() + value.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }
}
