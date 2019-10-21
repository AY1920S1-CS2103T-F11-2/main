package io.xpire.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import io.xpire.model.item.SortedUniqueToBuyItemList;
import io.xpire.model.item.ToBuyItem;
import javafx.collections.ObservableList;

/**
 * Wraps all the to-buy items at Replenish List level.
 * No duplicate items allowed.
 */
public class ReplenishList {

    private final SortedUniqueToBuyItemList items = new SortedUniqueToBuyItemList();

    public ReplenishList() {}

    /**
     * Creates a ReplenishList object using the ToBuyItems in the {@code toBeCopied}
     */
    public ReplenishList(ReplenishList toBeCopied) {
        this();
        this.resetData(toBeCopied);
    }

    /**
     * Replaces the contents of the replenish list with {@code items}.
     * {@code items} must not contain duplicate items.
     */
    public void setItems(List<ToBuyItem> items) {
        this.items.setItems(items);
    }

    /**
     * Returns true if a {@code ToBuyItem} with the same identity as {@code ToBuyItem} exists in the replenish list.
     */
    public boolean hasItem(ToBuyItem item) {
        requireNonNull(item);
        return this.items.contains(item);
    }

    /**
     * Removes {@code key} from this {@code ReplenishList}.
     * {@code key} must exist in the replenish list.
     */
    public void removeItem(ToBuyItem key) {
        this.items.remove(key);
    }

    /**
     * Resets the existing data of this {@code ReplenishList} with {@code newData}.
     */
    public void resetData(ReplenishList newData) {
        requireNonNull(newData);
        this.setItems(newData.getItemList());
    }

    /**
     * Adds an item to the replenish list.
     * The item must not already exist in the replenish list.
     */
    public void addItem(ToBuyItem item) {
        this.items.add(item);
    }

    /**
     * Replaces the given item {@code target} in the list with {@code editedItem}.
     * {@code target} must exist in the replenish list.
     * The item identity of {@code editedItem} must not be the same as another existing item in replenish
     * list.
     */
    public void setItem(ToBuyItem targetItem, ToBuyItem editedItem) {
        requireNonNull(editedItem);
        this.items.setItem(targetItem, editedItem);
    }

    /**
     * Returns an unmodifiable view of the replenish list.
     * This list will not contain any duplicate items.
     */
    public ObservableList<ToBuyItem> getItemList() {
        return this.items.asUnmodifiableObservableList();
    }

}
