package io.xpire.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;

public class ReplenishList {

    private final SortedUniqueToBuyItemList items = new SortedUniqueToBuyItemList();

    public ReplenishList() {}

    /**
     * Creates a Xpire object using the Items in the {@code toBeCopied}
     */
    public ReplenishList(ReplenishList toBeCopied) {
        this();
        this.resetData(toBeCopied);
    }

    /**
     * Replaces the contents of the item list with {@code items}.
     * {@code items} must not contain duplicate items.
     */
    public void setItems(List<ToBuyItem> items) {
        this.items.setItems(items);
    }

    /**
     * Returns true if an item with the same identity as {@code item} exists in xpire.
     */
    public boolean hasItem(ToBuyItem item) {
        requireNonNull(item);
        return this.items.contains(item);
    }

    /**
     * Removes {@code key} from this {@code Xpire}.
     * {@code key} must exist in xpire.
     */
    public void removeItem(ToBuyItem key) {
        this.items.remove(key);
    }

    /**
     * Resets the existing data of this {@code Xpire} with {@code newData}.
     */
    public void resetData(ReplenishList newData) {
        requireNonNull(newData);
        this.setItems(newData.getItemList());
    }

    /**
     * Adds a item to xpire.
     * The item must not already exist in xpire.
     */
    public void addItem(ToBuyItem item) {
        this.items.add(item);
    }

    /**
     * Replaces the given item {@code target} in the list with {@code editedItem}.
     * {@code target} must exist in xpire.
     * The item identity of {@code editedItem} must not be the same as another existing item in xpire.
     */
    public void setItem(ToBuyItem targetItem, ToBuyItem editedItem) {
        requireNonNull(editedItem);
        this.items.setItem(targetItem, editedItem);
    }


    public ObservableList<ToBuyItem> getItemList() {
        return this.items.asUnmodifiableObservableList();
    }

}
