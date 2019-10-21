package io.xpire.model.item;

import static io.xpire.model.item.Quantity.DEFAULT_QUANTITY;
import static io.xpire.model.item.ReminderThreshold.DEFAULT_THRESHOLD;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import io.xpire.commons.util.CollectionUtil;
import io.xpire.commons.util.DateUtil;
import io.xpire.model.tag.Tag;
import io.xpire.model.tag.TagComparator;

/**
 * Represents a Item in Xpire.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Item extends XpireItem {
    // Identity fields
    private final Name name;
    private final ExpiryDate expiryDate;

    // Data fields
    private Quantity quantity = new Quantity(DEFAULT_QUANTITY);
    private Set<Tag> tags = new TreeSet<>(new TagComparator());
    private ReminderThreshold reminderThreshold = new ReminderThreshold(DEFAULT_THRESHOLD);

    /**
     * Every field must be present and not null.
     * Only called in Tag and Edit commands.
     */
    public Item(Name name, ExpiryDate expiryDate, Quantity quantity, Set<Tag> tags) {
        CollectionUtil.requireAllNonNull(name, expiryDate, tags);
        this.name = name;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.tags.addAll(tags);
    }

    /**
     * Every field must be present and not null.
     * Tags are optional.
     */
    public Item(Name name, ExpiryDate expiryDate, Quantity quantity) {
        CollectionUtil.requireAllNonNull(name, expiryDate);
        this.name = name;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
    }

    /**
     * Every field must be present and not null.
     * Quantity is optional.
     */
    public Item(Name name, ExpiryDate expiryDate) {
        CollectionUtil.requireAllNonNull(name, expiryDate);
        this.name = name;
        this.expiryDate = expiryDate;
    }

    /**
     * Constructor with all parameters for ItemBuilder class. (Used in testing)
     */
    public Item(Name name, ExpiryDate expiryDate, Quantity quantity, Set<Tag> tags,
                ReminderThreshold reminderThreshold) {
        CollectionUtil.requireAllNonNull(name, expiryDate, tags);
        this.name = name;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.tags.addAll(tags);
        this.reminderThreshold = reminderThreshold;
    }

    public Item(Item item) {
        this.name = item.getName();
        this.expiryDate = item.getExpiryDate();
        this.quantity = item.getQuantity();
        this.tags = item.getTags();
        this.reminderThreshold = item.getReminderThreshold();
    }

    @Override
    public Name getName() {
        return this.name;
    }

    public ExpiryDate getExpiryDate() {
        return this.expiryDate;
    }

    public Quantity getQuantity() {
        return this.quantity;
    }

    /**
     * Sets and overrides the quantity.
     *
     * @param newQuantity Quantity to be updated.
     */
    public void setQuantity(Quantity newQuantity) {
        this.quantity = newQuantity;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    @Override
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(this.tags);
    }

    @Override
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    /**
     * Returns the reminder threshold.
     *
     * @return {@code ReminderThreshold} object.
     */
    public ReminderThreshold getReminderThreshold() {
        return this.reminderThreshold;
    }

    /**
     * Sets and overrides the reminder threshold.
     *
     * @param reminderThreshold reminder threshold.
     */
    public void setReminderThreshold(ReminderThreshold reminderThreshold) {
        this.reminderThreshold = reminderThreshold;
    }

    public boolean isItemExpired() {
        return this.expiryDate.isExpired(DateUtil.getCurrentDate());
    }

    @Override
    public boolean isSameItem(XpireItem other) {
        Item otherItem;
        try {
            otherItem = (Item) other;
        } catch (ClassCastException e) {
            return false;
        }
        return isSameItem(otherItem);
    }
    /**
     * Returns true if both items of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two items.
     */
    public boolean isSameItem(Item other) {
        if (other == this) {
            return true;
        } else {
            return other != null
                    && this.name.equals(other.name)
                    && this.expiryDate.equals(other.expiryDate);
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
                    && this.expiryDate.equals(other.expiryDate)
                    && this.tags.equals(other.tags)
                    && this.quantity.equals(other.quantity)
                    && this.reminderThreshold.equals(other.reminderThreshold);
        }
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(this.name, this.expiryDate, this.tags, this.quantity, this.reminderThreshold);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        if (!this.getTags().isEmpty()) {
            builder.append(this.name).append("\n")
                    .append(String.format("Expiry date: %s (%s)\n",
                            this.expiryDate, this.expiryDate.getStatus(DateUtil.getCurrentDate())))
                    .append("Tags: ");
        } else {
            builder.append(this.name).append("\n")
                    .append(String.format("Expiry date: %s (%s)\n",
                            this.expiryDate, this.expiryDate.getStatus(DateUtil.getCurrentDate())));
        }
        this.getTags().forEach(builder::append);
        return builder.toString();
    }
}
