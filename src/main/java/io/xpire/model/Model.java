package io.xpire.model;

import io.xpire.commons.core.GuiSettings;

import io.xpire.model.item.Item;
import io.xpire.model.item.Name;
import io.xpire.model.item.sort.MethodOfSorting;
import io.xpire.model.tag.Tag;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public interface Model<T extends Item> {
    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' xpire file path.
     */
    Path getListFilePath();

    /**
     * Sets the user prefs' xpire file path.
     */
    void setListFilePath(Path listFilePath);

    /**
     * Replaces xpire data with the data in {@code xpire}.
     */
    void setListView(ReadOnlyListView listView);

    /** Returns the xpire */
    ReadOnlyListView getListView();

    /**
     * Returns true if an item with the same identity as {@code item} exists in xpire.
     */
    boolean hasItem(T item);

    /**
     * Deletes the given item.
     * The item must exist in xpire.
     */
    void deleteItem(T target);

    /**
     * Adds the given item.
     * {@code item} must not already exist in xpire.
     */
    void addItem(T item);

    /**
     * Replaces the given item {@code target} with {@code editedItem}.
     * {@code target} must exist in xpire.
     * The item identity of {@code editedItem} must not be the same as another existing item in xpire.
     */
    void setItem(T target, T edited);

    /**
     * Sorts the filtered item list.
     * @param method The method of sorting.
     */
    void sortItemList(MethodOfSorting<T> method);

    /**
     * Returns a set containing all existing names of items in the list.
     * @return The set of all existing names.
     */
    Set<Name> getAllItemNames();

    /**
     * Returns a set containing all existing tags of items in the list.
     * @return The set of all existing tags.
     */
    Set<Tag> getAllItemTags();

    /** Returns an unmodifiable view of the filtered item list */
    ObservableList<T> getFilteredItemList();

    /**
     * Returns a list of all the items.
     *
     * @return List of all items.
     */
    List<T> getAllItemList();

    /**
     * Updates the filter of the filtered item list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredItemList(Predicate<T> predicate);

}
