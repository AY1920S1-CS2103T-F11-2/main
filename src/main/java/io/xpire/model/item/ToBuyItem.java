package io.xpire.model.item;

import java.util.Set;
import java.util.TreeSet;

import io.xpire.commons.util.CollectionUtil;
import io.xpire.model.tag.Tag;
import io.xpire.model.tag.TagComparator;

/**
 * Represents an item user needs to replenish.
 */
public class ToBuyItem extends XpireItem {

    private final Name name;
    private Set<Tag> tags = new TreeSet<>(new TagComparator());

    public ToBuyItem(Name name, Set<Tag> tags) {
        CollectionUtil.requireAllNonNull(name, tags);
        this.name = name;
        this.tags.addAll(tags);
    }

    @Override
    public Name getName() {
        return this.name;
    }

    @Override
    public Set<Tag> getTags() {
        return this.tags;
    }

    @Override
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean isSameItem(XpireItem other) {
        ToBuyItem otherItem;
        try {
            otherItem = (ToBuyItem) other;
        } catch (ClassCastException e) {
            return false;
        }
        return isSameItem(otherItem);
    }
    /**
     * Returns true if both items of the same name have at least one other identity field that is the same.
     */
    public boolean isSameItem(ToBuyItem other) {
        if (other == this) {
            return true;
        } else {
            return other != null
                    && this.name.equals(other.name);
        }
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
