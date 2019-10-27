package io.xpire.model.item;

import io.xpire.commons.util.CollectionUtil;
import io.xpire.model.tag.Tag;
import io.xpire.model.tag.TagComparator;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents an item in the replenish list.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Item {

    //identity fields
    final Name name;

    //data fields
    Set<Tag> tags = new TreeSet<>(new TagComparator());

    /**
     * Every field must be present and not null.
     */
    public Item(Name name, Set<Tag> tags) {
        CollectionUtil.requireAllNonNull(name, tags);
        this.name = name;
        this.tags.addAll(tags);
    }

    /**
     * Every field must be present and not null.
     */
    public Item(Name name) {
        CollectionUtil.requireAllNonNull(name);
        this.name = name;
    }

    /**
     * Every field must be present and not null.
     * Used for testing.
     */
    public Item(Item item) {
        this.name = item.getName();
        this.tags = item.getTags();
    }

    public Name getName() {
        return this.name;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(this.tags);
    }

    /**
     * Sets and overrides the tags.
     *
     * @param tags tags.
     */
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Returns true if both items are of the same name.
     * This defines a weaker notion of equality between two items.
     */
    public boolean isSameItem(Item other) {
        if (other == this) {
            return true;
        } else {
            return other != null
                    && this.name.equals(other.name);
        }
    }

    /**
     * Returns true if both items have the same identity and data fields.
     * This defines a stronger notion of equality between two items.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Item)) {
            return false;
        } else {
            Item other = (Item) obj;
            return this.name.equals(other.name)
                    && this.tags.equals(other.tags);
        }
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(this.name, this.tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        if (!this.getTags().isEmpty()) {
            builder.append(this.name).append("\n")
                    .append("Tags: ");
        } else {
            builder.append(this.name).append("\n");
        }
        this.getTags().forEach(builder::append);
        return builder.toString();
    }
}