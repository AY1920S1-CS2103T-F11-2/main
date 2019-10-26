package io.xpire.model;

import io.xpire.model.item.Item;
import io.xpire.model.item.sort.MethodOfSorting;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.function.Predicate;

public interface ReplenishModel extends Model<Item> {

    /** {@code Predicate} that always evaluate to true */
    Predicate<Item> PREDICATE_SHOW_ALL_ITEMS = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<Item> PREDICATE_SORT_ALL_ITEMS = unused -> true;


    /**
     * Returns true if an item with the same identity as {@code item} exists in xpire.
     */
    @Override
    boolean hasItem(Item xpireItem);

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
     * {@code target} must exist in xpire.
     * The item identity of {@code editedItem} must not be the same as another existing item in xpire.
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

    /**
     * Updates the filter of the filtered item list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    @Override
    void updateFilteredItemList(Predicate<Item> predicate);

}
