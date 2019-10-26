package io.xpire.model;

import io.xpire.model.item.Item;
import io.xpire.model.item.sort.MethodOfSorting;
import javafx.collections.ObservableList;

import java.util.List;

public interface ListModel extends Model<Item> {
    /**
     * Returns true if an item with the same identity as {@code item} exists in xpire.
     */
    @Override
    boolean hasItem(Item item);

    /**
     * Deletes the given item.
     * The item must exist in xpire.
     */
    @Override
    void deleteItem(Item target);

    /**
     * Adds the given item.
     * {@code item} must not already exist in xpire.
     */
    @Override
    void addItem(Item item);

    /**
     * Replaces the given item {@code target} with {@code editedItem}.
     * {@code target} must exist in the list.
     * The item identity of {@code editedItem} must not be the same as another existing item in list.
     */
    @Override
    void setItem(Item target, Item editedItem);

    /**
     * Sorts the filtered item list.
     * @param method The method of sorting.
     */
    @Override
    void sortItemList(MethodOfSorting<Item> method);


    /** Returns an unmodifiable view of the filtered item list */
    @Override
    ObservableList<Item> getFilteredItemList();

    /**
     * Returns a list of all the items.
     *
     * @return List of all items.
     */
    @Override
    List<Item> getAllItemList();

//    /**
//     * Updates the filter of the filtered item list to filter by the given {@code predicate}.
//     * @throws NullPointerException if {@code predicate} is null.
//     */
//    @Override
//    void updateFilteredItemList(Predicate<T> predicate);
}
