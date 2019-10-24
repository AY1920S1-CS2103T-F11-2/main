package io.xpire.model;

import java.nio.file.Path;

import java.util.List;
import java.util.Set;

import java.util.function.Predicate;

import io.xpire.commons.core.GuiSettings;
import io.xpire.model.item.XpireItem;
import io.xpire.model.item.Name;
import io.xpire.model.item.sort.XpireMethodOfSorting;
import io.xpire.model.tag.Tag;
import javafx.collections.ObservableList;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<XpireItem> PREDICATE_SHOW_ALL_ITEMS = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<XpireItem> PREDICATE_SORT_ALL_ITEMS = unused -> true;

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
    Path getXpireFilePath();

    /**
     * Sets the user prefs' xpire file path.
     */
    void setXpireFilePath(Path xpireFilePath);

    /**
     * Replaces xpire data with the data in {@code xpire}.
     */
    void setXpire(ReadOnlyXpire xpire);

    /** Returns the xpire */
    ReadOnlyXpire getXpire();

    /**
     * Returns true if an item with the same identity as {@code item} exists in xpire.
     */
    boolean hasItem(XpireItem xpireItem);

    /**
     * Deletes the given item.
     * The item must exist in xpire.
     */
    void deleteItem(XpireItem target);

    /**
     * Adds the given item.
     * {@code item} must not already exist in xpire.
     */
    void addItem(XpireItem xpireItem);

    /**
     * Replaces the given item {@code target} with {@code editedItem}.
     * {@code target} must exist in xpire.
     * The item identity of {@code editedItem} must not be the same as another existing item in xpire.
     */
    void setItem(XpireItem target, XpireItem editedXpireItem);

    /**
     * Sorts the filtered item list.
     * @param method The method of sorting.
     */
    void sortItemList(XpireMethodOfSorting method);

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
    ObservableList<XpireItem> getFilteredItemList();

    /**
     * Returns a list of all the items.
     *
     * @return List of all items.
     */
    List<XpireItem> getAllItemList();

    /**
     * Updates the filter of the filtered item list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredItemList(Predicate<XpireItem> predicate);

}
