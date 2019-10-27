package io.xpire.model.item;

import static io.xpire.model.item.Quantity.DEFAULT_QUANTITY;
import static io.xpire.model.item.ReminderThreshold.DEFAULT_THRESHOLD;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import io.xpire.commons.util.CollectionUtil;
import io.xpire.commons.util.DateUtil;
import io.xpire.model.tag.Tag;
import io.xpire.model.tag.TagComparator;

/**
 * Represents an item in the expiry date tracker.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class XpireItem extends Item {
    // Identity fields
    private final ExpiryDate expiryDate;
    private Set<Tag> tags = new TreeSet<>(new TagComparator());

    // Data fields
    private Quantity quantity = new Quantity(DEFAULT_QUANTITY);
    private ReminderThreshold reminderThreshold = new ReminderThreshold(DEFAULT_THRESHOLD);

    /**
     * Every field must be present and not null.
     * Only called in Tag and Delete commands.
     */
    public XpireItem(Name name, ExpiryDate expiryDate, Quantity quantity, Set<Tag> tags) {
        super(name, tags);
        CollectionUtil.requireAllNonNull(expiryDate);
        this.expiryDate = expiryDate;
        this.quantity = quantity;
    }

    /**
     * Every field must be present and not null.
     * Tags are optional.
     */
    public XpireItem(Name name, ExpiryDate expiryDate, Quantity quantity) {
        super(name);
        CollectionUtil.requireAllNonNull(expiryDate, quantity);
        this.expiryDate = expiryDate;
        this.quantity = quantity;
    }

    /**
     * Every field must be present and not null.
     * Quantity is optional.
     */
    public XpireItem(Name name, ExpiryDate expiryDate) {
        super(name);
        CollectionUtil.requireAllNonNull(expiryDate);
        this.expiryDate = expiryDate;
    }

    /**
     * Constructor with all parameters for ItemBuilder class. (Used in testing)
     */
    public XpireItem(Name name, ExpiryDate expiryDate, Quantity quantity, Set<Tag> tags,
                     ReminderThreshold reminderThreshold) {
        super(name, tags);
        CollectionUtil.requireAllNonNull(expiryDate);
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.reminderThreshold = reminderThreshold;
    }

    public XpireItem(XpireItem xpireItem) {
        super(xpireItem);
        this.expiryDate = xpireItem.getExpiryDate();
        this.quantity = xpireItem.getQuantity();
        this.reminderThreshold = xpireItem.getReminderThreshold();
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

    /**
     * Returns true if both items of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two items.
     */
    @Override
    public boolean isSameItem(Item otherItem) {
        XpireItem other;
        try {
            other = (XpireItem) otherItem;
        } catch (ClassCastException e) {
            return false;
        }
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
        } else if (!(obj instanceof XpireItem)) {
            return false;
        } else {
            XpireItem other = (XpireItem) obj;
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
