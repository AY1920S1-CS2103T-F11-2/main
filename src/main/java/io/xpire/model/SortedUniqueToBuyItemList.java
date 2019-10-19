package io.xpire.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import io.xpire.commons.util.CollectionUtil;
import io.xpire.model.item.exceptions.DuplicateItemException;
import io.xpire.model.item.exceptions.ItemNotFoundException;
import io.xpire.model.item.sort.MethodOfSortingToBuy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

public class SortedUniqueToBuyItemList {

    private final ObservableList<ToBuyItem> internalList = FXCollections.observableArrayList();
    private final SortedList<ToBuyItem> sortedInternalList = new SortedList<>(internalList);
    private final ObservableList<ToBuyItem> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(this.sortedInternalList);
    private MethodOfSortingToBuy methodOfSorting = new MethodOfSortingToBuy("name");

    /**
     * Returns true if the list contains an equivalent item as the given argument.
     */
    public boolean contains(ToBuyItem toCheck) {
        requireNonNull(toCheck);
        return this.internalList.stream().anyMatch(toCheck::isSameItem);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<ToBuyItem> asUnmodifiableObservableList() {
        this.sortedInternalList.setComparator(methodOfSorting.getComparator());
        return this.internalUnmodifiableList;
    }

    /**
     * Adds an item to the list.
     * The item must not already exist in the list.
     */
    public void add(ToBuyItem toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateItemException();
        }
        this.internalList.add(toAdd);
        methodOfSorting = new MethodOfSortingToBuy("name");
    }

    /**
     * Replaces the item { @code target} in the list with {@code editedItem}.
     * {@code target} must exist in the list.
     * The item identity of {@code editedItem} must not be the same as another existing item in the list.
     */
    public void setItem(ToBuyItem target, ToBuyItem editedItem) {
        CollectionUtil.requireAllNonNull(target, editedItem);

        int index = this.internalList.indexOf(target);
        if (index == -1) {
            throw new ItemNotFoundException();
        }

        if (!target.isSameItem(editedItem) && contains(editedItem)) {
            throw new DuplicateItemException();
        }

        this.internalList.set(index, editedItem);
    }

    public void setItems(SortedUniqueToBuyItemList replacement) {
        requireNonNull(replacement);
        this.internalList.setAll(replacement.sortedInternalList);
    }

    /**
     * Replaces the contents of this list with {@code items}.
     * {@code items} must not contain duplicate items.
     */
    public void setItems(List<ToBuyItem> items) {
        CollectionUtil.requireAllNonNull(items);
        if (!itemsAreUnique(items)) {
            throw new DuplicateItemException();
        }
        this.internalList.setAll(items);
    }

    /**
     * Removes the equivalent item from the list.
     * The item must exist in the list.
     */
    public void remove(ToBuyItem toRemove) {
        requireNonNull(toRemove);
        if (!this.internalList.remove(toRemove)) {
            throw new ItemNotFoundException();
        }
    }

    /**
     * Returns true if {@code items} contains only unique items.
     */
    private boolean itemsAreUnique(List<ToBuyItem> items) {
        return items.size() == items.stream().distinct().count();
    }


}
