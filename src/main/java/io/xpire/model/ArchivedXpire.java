package io.xpire.model;

import io.xpire.model.item.Item;
import javafx.collections.ObservableList;

/**
 * ArchiveXpire that stores the history.
 */
public class ArchivedXpire implements ReadOnlyXpire {
    @Override
    public ObservableList<Item> getItemList() {
        return null;
    }
}
