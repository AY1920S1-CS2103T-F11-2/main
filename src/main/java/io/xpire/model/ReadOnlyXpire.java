package io.xpire.model;

import io.xpire.model.item.XpireItem;
import javafx.collections.ObservableList;

/**
 * Unmodifiable view of an xpire.
 */
public interface ReadOnlyXpire {
    /**
     * Returns an unmodifiable view of the xpireItem list.
     * This list will not contain any duplicate items.
     */
    ObservableList<XpireItem> getItemList();
}
