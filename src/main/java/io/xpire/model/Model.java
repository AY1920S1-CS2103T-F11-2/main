package io.xpire.model;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import io.xpire.commons.core.GuiSettings;
import io.xpire.model.item.Item;
import io.xpire.model.item.Name;
import io.xpire.model.item.XpireItem;
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

    /** {@code Predicate} that always evaluate to true */
    Predicate<Item> PREDICATE_SHOW_ALL_REPLENISH_ITEMS = unused -> true;


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
    void setListFilePath(Path xpireFilePath);

    /**
     * Returns an array containing xpire and replenish list.
     */
    ReadOnlyListView<? extends Item>[] getLists();

    /**
     * Replaces xpire data with the data in {@code xpire}.
     */
    void setXpire(ReadOnlyListView<XpireItem> xpire);


    /**
     * Returns an Xpire object.
     */
    ReadOnlyListView<XpireItem> getXpire();

    /**
     * Returns true if an xpireItem with the same identity as {@code xpireItem} exists in xpire.
     */
    boolean hasItem(XpireItem xpireItem);

    /**
     * Deletes the given xpireItem.
     * The xpireItem must exist in xpire.
     */
    void deleteItem(XpireItem target);

    /**
     * Adds the given xpireItem.
     * {@code xpireItem} must not already exist in xpire.
     */
    void addItem(XpireItem xpireItem);

    /**
     * Replaces the given xpireItem {@code target} with {@code editedXpireItem}.
     * {@code target} must exist in xpire.
     * The xpireItem identity of {@code editedXpireItem} must not be the same as another existing xpireItem in xpire.
     */
    void setItem(XpireItem target, XpireItem editedXpireItem);

    /**
     * Sorts the filtered xpireItem list.
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

    /** Returns an unmodifiable view of the filtered xpireItem list */
    ObservableList<XpireItem> getFilteredItemList();

    /**
     * Returns a list of all the items.
     *
     * @return List of all items.
     */
    List<XpireItem> getAllItemList();

    /**
     * Updates the filter of the filtered xpireItem list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredItemList(Predicate<XpireItem> predicate);

    /**
     * Replaces replenish list data with the data in {@code replenishList}.
     */
    void setReplenishList(ReadOnlyListView<Item> replenishList);

    ReadOnlyListView<Item> getReplenishList();

    boolean hasReplenishItem(Item item);

    void deleteReplenishItem(Item target);

    void addReplenishItem(Item item);

    void setReplenishItem(Item target, Item editedItem);

    Set<Tag> getAllReplenishItemTags();

    Set<Name> getAllReplenishItemNames();

    void updateFilteredReplenishItemList(Predicate<Item> predicate);

    List<Item> getReplenishItemList();

    void shiftItemToReplenishList(XpireItem xpireItem);

    void addItemToReplenishList(XpireItem xpireItem);

    void checkItemsForShift();

}
