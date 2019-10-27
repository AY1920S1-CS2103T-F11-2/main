package io.xpire.storage;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.xpire.commons.exceptions.IllegalValueException;
import io.xpire.model.ReadOnlyListView;
import io.xpire.model.ReplenishList;
import io.xpire.model.item.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An Immutable Xpire that is serializable to JSON format.
 */
@JsonRootName(value = "xpire")
class JsonSerializableReplenishList {

    public static final String MESSAGE_DUPLICATE_ITEM = "Items list contains duplicate item(s).";

    private final List<JsonAdaptedItem> items = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableReplenishList} with the given items.
     */
    @JsonCreator
    public JsonSerializableReplenishList(@JsonProperty("replenishList") List<JsonAdaptedItem> items) {
        this.items.addAll(items);
    }

    /**
     * Converts a given {@code ReadOnlyListView} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableXpire}.
     */
    public JsonSerializableReplenishList(ReadOnlyListView<Item> source) {
        items.addAll(source.getItemList().stream().map(JsonAdaptedItem::new).collect(Collectors.toList()));
    }

    /**
     * Converts this expiry date tracker into the xpireModel's {@code ExpiryDateTracker} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public ReplenishList toModelType() throws IllegalValueException {
        ReplenishList replenishList = new ReplenishList();
        for (JsonAdaptedItem jsonAdaptedItem : items) {
            Item item = jsonAdaptedItem.toModelType();
            if (replenishList.hasItem(item)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_ITEM);
            }
            replenishList.addItem(item);
        }
        return replenishList;
    }

}