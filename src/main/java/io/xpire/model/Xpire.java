package io.xpire.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import io.xpire.model.item.XpireItem;
import io.xpire.model.item.SortedUniqueItemList;
import io.xpire.model.item.sort.XpireMethodOfSorting;
import javafx.collections.ObservableList;

/**
 * Wraps all data at xpire level
 * Duplicates are not allowed (by .isSameItem comparison)
 */
public class Xpire implements ReadOnlyXpire {

    private final SortedUniqueItemList items = new SortedUniqueItemList();

    public Xpire() {}

    /**
     * Creates a Xpire object using the Items in the {@code toBeCopied}
     */
    public Xpire(ReadOnlyXpire toBeCopied) {
        this();
        this.resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the xpireItem list with {@code xpireItems}.
     * {@code xpireItems} must not contain duplicate xpireItems.
     */
    public void setItems(List<XpireItem> xpireItems) {
        this.items.setItems(xpireItems);
    }

    /**
     * Resets the existing data of this {@code Xpire} with {@code newData}.
     */
    public void resetData(ReadOnlyXpire newData) {
        requireNonNull(newData);
        this.setItems(newData.getItemList());
    }

    /**
     * Returns true if an xpireItem with the same identity as {@code xpireItem} exists in xpire.
     */
    public boolean hasItem(XpireItem xpireItem) {
        requireNonNull(xpireItem);
        return this.items.contains(xpireItem);
    }

    /**
     * Adds a xpireItem to xpire.
     * The xpireItem must not already exist in xpire.
     */
    public void addItem(XpireItem xpireItem) {
        this.items.add(xpireItem);
    }

    /**
     * Replaces the given xpireItem {@code target} in the list with {@code editedXpireItem}.
     * {@code target} must exist in xpire.
     * The xpireItem identity of {@code editedXpireItem} must not be the same as another existing xpireItem in xpire.
     */
    public void setItem(XpireItem target, XpireItem editedXpireItem) {
        requireNonNull(editedXpireItem);
        this.items.setItem(target, editedXpireItem);
    }

    /**
     * Removes {@code key} from this {@code Xpire}.
     * {@code key} must exist in xpire.
     */
    public void removeItem(XpireItem key) {
        this.items.remove(key);
    }

    /**
     * Set method of sorting.
     */
    public void setMethodOfSorting(XpireMethodOfSorting method) {
        this.items.setXpireMethodOfSorting(method);
    }

    //// util methods

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.items.asUnmodifiableObservableList().size() + " items");
        this.items.asUnmodifiableObservableList().forEach(x-> sb.append(x));
        return sb.toString();
    }

    @Override
    public ObservableList<XpireItem> getItemList() {
        return this.items.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Xpire)) {
            return false;
        } else {
            Xpire other = (Xpire) obj;
            return this.items.equals(other.items);
        }
    }

    @Override
    public int hashCode() {
        return this.items.hashCode();
    }
}
