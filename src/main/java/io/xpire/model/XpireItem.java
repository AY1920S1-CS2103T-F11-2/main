package io.xpire.model;

import java.util.Set;

import io.xpire.model.item.Name;
import io.xpire.model.tag.Tag;

public abstract class XpireItem {

    public abstract Name getName();

    public abstract Set<Tag> getTags();

    /**
     * Sets and overrides the tags.
     *
     * @param tags tags.
     */
    public abstract void setTags(Set<Tag> tags);

    public abstract boolean isSameItem(XpireItem other);
}
