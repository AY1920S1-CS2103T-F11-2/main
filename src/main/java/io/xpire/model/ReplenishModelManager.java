package io.xpire.model;

import io.xpire.commons.core.GuiSettings;
import io.xpire.commons.core.LogsCenter;
import io.xpire.commons.util.CollectionUtil;
import io.xpire.model.item.Item;
import io.xpire.model.item.Name;
import io.xpire.model.item.sort.MethodOfSorting;
import io.xpire.model.tag.Tag;
import io.xpire.model.tag.TagComparator;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

public class ReplenishModelManager implements ReplenishModel {

    private static final Logger logger = LogsCenter.getLogger(ReplenishModelManager.class);

    private final ReplenishList replenishList;
    private final UserPrefs userPrefs;
    private final FilteredList<Item> filteredReplenishItems;

    /**
     * Initializes a XpireModelManager with the given xpire and userPrefs.
     */
    public ReplenishModelManager(ReadOnlyListView replenish, ReadOnlyUserPrefs userPrefs) {
        super();
        CollectionUtil.requireAllNonNull(replenish, userPrefs);

        logger.fine("Initializing with replenish list: " + replenish + " and user prefs " + userPrefs);

        this.replenishList = new ReplenishList(replenish);
        this.userPrefs = new UserPrefs(userPrefs);
        this.filteredReplenishItems = new FilteredList<>(this.replenishList.getItemList());
    }

    public ReplenishModelManager() {
        this(new ReplenishList(), new UserPrefs());
    }

    //=========== UserPrefs =========================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return this.userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return this.userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getListFilePath() {
        return this.userPrefs.getListFilePath();
    }

    @Override
    public void setListFilePath(Path xpireFilePath) {
        requireNonNull(xpireFilePath);
        this.userPrefs.setListFilePath(xpireFilePath);
    }

    //=========== expiryDateTracker  ================================================================================

    @Override
    public void setListView(ReadOnlyListView replenish) {
        this.replenishList.resetData(replenish);
    }

    @Override
    public ReadOnlyListView getListView() {
        return this.replenishList;
    }

    @Override
    public boolean hasItem(Item item) {
        requireNonNull(item);
        return this.replenishList.hasItem(item);
    }

    @Override
    public void deleteItem(Item target) {
        this.replenishList.removeItem(target);
    }

    @Override
    public void addItem(Item item) {
        this.replenishList.addItem(item);
        updateFilteredItemList(PREDICATE_SHOW_ALL_ITEMS);
    }

    @Override
    public void setItem(Item target, Item editedItem) {
        CollectionUtil.requireAllNonNull(target, editedItem);
        this.replenishList.setItem(target, editedItem);
    }

    @Override
    public Set<Tag> getAllItemTags() {
        Set<Tag> tagSet = new TreeSet<>(new TagComparator());
        List<Item> itemList = this.replenishList.getItemList();
        itemList.forEach(item -> tagSet.addAll(item.getTags()));
        return tagSet;
    }

    @Override
    public Set<Name> getAllItemNames() {
        Set<Name> nameSet = new TreeSet<>(Comparator.comparing(Name::toString));
        List<Item> itemList = this.replenishList.getItemList();
        itemList.forEach(item -> nameSet.add(item.getName()));
        return nameSet;
    }


    //=========== Sorted Item List Accessors ========================================================================

    @Override
    public void sortItemList(MethodOfSorting<Item> method) {
        requireNonNull(method);
        this.replenishList.setMethodOfSorting(method);
    }

    // =========== Filtered Item List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Item} backed by the internal list of
     * {@code versionedXpire}
     */
    @Override
    public ObservableList<Item> getFilteredItemList() {
        return this.filteredReplenishItems;
    }

    @Override
    public void updateFilteredItemList(Predicate<Item> predicate) {
        requireNonNull(predicate);
        Predicate<? super Item> p = this.filteredReplenishItems.getPredicate();
        if (predicate == PREDICATE_SHOW_ALL_ITEMS || p == null) {
            // a view command or first ever search command
            this.filteredReplenishItems.setPredicate(predicate);
        } else {
            // search commands have been executed before
            this.filteredReplenishItems.setPredicate(predicate.and(p));
        }
    }

    // =========== Tag Item List Accessors =============================================================

    @Override
    public List<Item> getAllItemList() {
        return this.replenishList.getItemList();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof ReplenishModelManager)) {
            return false;
        } else {
            ReplenishModelManager other = (ReplenishModelManager) obj;
            return this.replenishList.equals(other.replenishList)
                    && this.userPrefs.equals(other.userPrefs)
                    && this.filteredReplenishItems.equals(other.filteredReplenishItems);
        }
    }
}
