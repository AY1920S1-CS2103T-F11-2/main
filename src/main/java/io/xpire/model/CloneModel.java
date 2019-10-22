package io.xpire.model;

import io.xpire.model.item.Item;
import io.xpire.model.item.SortedUniqueItemList;
import javafx.collections.ObservableList;

/**
 * A clone of a model.
 */
public class CloneModel {

    private SortedUniqueItemList fullItemList = null;
    private UserPrefs userPref = null;
    private ObservableList<Item> filteredList = null;

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

    private void cloneFilteredList(Model model) {
        this.filteredList = model.getFilteredItemList();
        this.filteredList.forEach(item -> new Item(item));
    }

    /**
     * Retrieves the filtered list of items for this current model and state.
     */
    public ObservableList<Item> getFilteredList() {
        return this.filteredList;
    }

}
