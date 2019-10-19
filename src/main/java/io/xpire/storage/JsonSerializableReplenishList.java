package io.xpire.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.xpire.commons.exceptions.IllegalValueException;
import io.xpire.model.ReplenishList;
import io.xpire.model.ToBuyItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An Immutable Xpire that is serializable to JSON format.
 */
@JsonRootName(value = "replenishList")
class JsonSerializableReplenishList {

        public static final String MESSAGE_DUPLICATE_ITEM = "Items list contains duplicate item(s).";

        private final List<JsonAdaptedToBuyItem> items = new ArrayList<>();

        /**
         * Constructs a {@code JsonSerializableXpire} with the given items.
         */
        @JsonCreator
        public JsonSerializableReplenishList(@JsonProperty("items") List<JsonAdaptedToBuyItem> items) {
            this.items.addAll(items);
        }

        /**
         * Converts a given {@code ReadOnlyXpire} into this class for Jackson use.
         *
         * @param source future changes to this will not affect the created {@code JsonSerializableXpire}.
         */
        public JsonSerializableReplenishList(ReplenishList source) {
            items.addAll(source.getItemList().stream().map(JsonAdaptedToBuyItem::new).collect(Collectors.toList()));
        }

        /**
         * Converts this expiry date tracker into the model's {@code ExpiryDateTracker} object.
         *
         * @throws IllegalValueException if there were any data constraints violated.
         */
        public ReplenishList toModelType() throws IllegalValueException {
            ReplenishList replenishList = new ReplenishList();
            for (JsonAdaptedToBuyItem jsonAdaptedItem : items) {
                ToBuyItem item = jsonAdaptedItem.toModelType();
                if (replenishList.hasItem(item)) {
                    throw new IllegalValueException(MESSAGE_DUPLICATE_ITEM);
                }
                replenishList.addItem(item);
            }
            return replenishList;
        }

}
