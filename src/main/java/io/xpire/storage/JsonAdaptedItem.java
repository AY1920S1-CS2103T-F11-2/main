package io.xpire.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.xpire.commons.exceptions.IllegalValueException;
import io.xpire.model.item.ExpiryDate;
import io.xpire.model.item.XpireItem;
import io.xpire.model.item.Name;
import io.xpire.model.item.Quantity;
import io.xpire.model.item.ReminderThreshold;
import io.xpire.model.tag.Tag;
import io.xpire.model.tag.TagComparator;

/**
 * Jackson-friendly version of {@link XpireItem}.
 */
class JsonAdaptedItem {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Item's %s field is missing!";

    private final String name;
    private final String expiryDate;
    private final String quantity;
    private final String reminderThreshold;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedItem} with the given item details.
     */
    @JsonCreator
    public JsonAdaptedItem(@JsonProperty("name") String name,
                           @JsonProperty("expiryDate") String expiryDate,
                           @JsonProperty("quantity") String quantity,
                           @JsonProperty("reminderThreshold") String reminderThreshold,
                           @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.name = name;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.reminderThreshold = reminderThreshold;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Item} into this class for Jackson use.
     */
    public JsonAdaptedItem(XpireItem source) {
        this.name = source.getName().toString();
        this.expiryDate = source.getExpiryDate().toString();
        this.quantity = source.getQuantity().toString();
        this.reminderThreshold = source.getReminderThreshold().toString();
        this.tags.addAll(source
                .getTags()
                .stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted item object into the model's {@code Item} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted item.
     */
    public XpireItem toModelType() throws IllegalValueException {

        if (this.name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(this.name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(this.name);

        if (this.expiryDate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    ExpiryDate.class.getSimpleName()));
        }
        if (!ExpiryDate.isValidFormatExpiryDate(this.expiryDate)) {
            throw new IllegalValueException(ExpiryDate.MESSAGE_CONSTRAINTS_FORMAT);
        }

        final ExpiryDate modelExpiryDate = new ExpiryDate(this.expiryDate);

        if (this.quantity == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Quantity.class.getSimpleName()));
        }
        if (!Quantity.isValidInputQuantity(this.quantity)) {
            throw new IllegalValueException(Quantity.MESSAGE_CONSTRAINTS);
        }
        final Quantity modelQuantity = new Quantity(this.quantity);

        if (this.reminderThreshold == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    ReminderThreshold.class.getSimpleName()));
        }

        if (!ReminderThreshold.isValidReminderThreshold(this.reminderThreshold)) {
            throw new IllegalValueException(ReminderThreshold.MESSAGE_CONSTRAINTS);
        }
        final ReminderThreshold modelReminderThreshold = new ReminderThreshold(this.reminderThreshold);

        final List<Tag> itemTags = new ArrayList<>();
        for (JsonAdaptedTag tag : this.tags) {
            itemTags.add(tag.toModelType());
        }
        final Set<Tag> modelTags = new TreeSet<>(new TagComparator());
        modelTags.addAll(itemTags);

        XpireItem xpireItem = new XpireItem(modelName, modelExpiryDate, modelQuantity, modelTags);
        xpireItem.setReminderThreshold(modelReminderThreshold);
        return xpireItem;
    }
}
