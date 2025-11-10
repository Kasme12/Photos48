package model;

import java.io.Serializable;

public class Tag implements Serializable {
    private String name;
    private String value;

    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() { return name; }
    public String getValue() { return value; }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tag)) return false;
        Tag other = (Tag) obj;
        return name.equalsIgnoreCase(other.name) && value.equalsIgnoreCase(other.value);
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
