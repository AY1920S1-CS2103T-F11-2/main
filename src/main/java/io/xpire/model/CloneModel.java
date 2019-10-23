package io.xpire.model;

import java.util.function.Predicate;

import io.xpire.model.item.Item;
import io.xpire.model.item.SortedUniqueItemList;

/**
 * A clone of a model.
 */
public class CloneModel {

    private SortedUniqueItemList fullItemList = null;
    private UserPrefs userPref = null;
    private SortedUniqueItemList filteredList = null;
    private Predicate<? super Item> predicate = null;

    public CloneModel(Model model) {
        cloneXpire(model);
        cloneUserPrefs(model);
        cloneFilteredList(model);
    }

    /**
     * Clones Xpire from model.
     */
    private void cloneXpire(Model model) {
        Xpire xpire = (Xpire) model.getXpire();
        this.fullItemList = new SortedUniqueItemList();
        for (Item item: xpire.getItemList()) {
            this.fullItemList.add(new Item(item));
        }
    }

    /**
     * Retrieves the full item list for this current model and state.
     */
    public SortedUniqueItemList getFullItemList() {
        return this.fullItemList;
    }

    private void cloneUserPrefs(Model model) {
        this.userPref = new UserPrefs(model.getUserPrefs());
    }

    /**
     * Retrieves the user preferences for this current model and state.
     */
    public UserPrefs getUserPref() {
        return this.userPref;
    }

    /**
     * Clones the filtered list in the model.
     */
    private void cloneFilteredList(Model model) {
        Predicate<? super Item> predicate = model.getFilteredItemList(true).getPredicate();
        this.filteredList = new SortedUniqueItemList();
        for (Item item: model.getFilteredItemList()) {
            this.filteredList.add(new Item(item));
        }
    }

    /**
     * Retrieves the filtered list of items for this current model and state.
     */
    public SortedUniqueItemList getFilteredList() {
        return this.filteredList;
    }

    /**
     * Retrieves the predicate associated with this state's filtered list.
     */
    public Predicate<? super Item> getPredicate() {
        return this.predicate;
    }

}
