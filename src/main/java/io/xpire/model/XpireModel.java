package io.xpire.model;

import java.util.List;

import java.util.function.Predicate;

import io.xpire.model.item.XpireItem;
import io.xpire.model.item.sort.MethodOfSorting;

import javafx.collections.ObservableList;

/**
 * The API of the XpireModel component.
 */
public interface XpireModel extends Model<XpireItem> {
    /** {@code Predicate} that always evaluate to true */
    Predicate<XpireItem> PREDICATE_SHOW_ALL_ITEMS = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<XpireItem> PREDICATE_SORT_ALL_ITEMS = unused -> true;

    /**
     * Returns true if an item with the same identity as {@code item} exists in xpire.
     */
    @Override
    boolean hasItem(XpireItem xpireItem);

    /**
     * Deletes the given item.
     * The item must exist in xpire.
     */
    @Override
    void deleteItem(XpireItem target);

    /**
     * Adds the given item.
     * {@code item} must not already exist in xpire.
     */
    @Override
    void addItem(XpireItem xpireItem);

    /**
     * Replaces the given item {@code target} with {@code editedItem}.
     * {@code target} must exist in xpire.
     * The item identity of {@code editedItem} must not be the same as another existing item in xpire.
     */
    @Override
    void setItem(XpireItem target, XpireItem editedXpireItem);

    /**
     * Sorts the filtered item list.
     * @param method The method of sorting.
     */
    @Override
    void sortItemList(MethodOfSorting<XpireItem> method);


    /** Returns an unmodifiable view of the filtered item list */
    @Override
    ObservableList<XpireItem> getFilteredItemList();

    /**
     * Returns a list of all the items.
     *
     * @return List of all items.
     */
    @Override
    List<XpireItem> getAllItemList();

    /**
     * Updates the filter of the filtered item list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    @Override
    void updateFilteredItemList(Predicate<XpireItem> predicate);

}
