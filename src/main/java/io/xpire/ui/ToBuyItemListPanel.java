package io.xpire.ui;


import io.xpire.commons.core.LogsCenter;
import io.xpire.model.item.ToBuyItem;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

import java.util.logging.Logger;

/**
 * Panel containing the list of items.
 */
public class ToBuyItemListPanel extends UiPart<Region> {
    private static final String FXML = "ToBuyItemListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ItemListPanel.class);

    @javafx.fxml.FXML
    private ListView<ToBuyItem> itemListView;

    public ToBuyItemListPanel(ObservableList<ToBuyItem> itemList) {
        super(FXML);
        itemListView.setItems(itemList);
        itemListView.setCellFactory(listView -> new ToBuyItemListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Item} using a {@code ItemCard}.
     */
    class ToBuyItemListViewCell extends ListCell<ToBuyItem> {
        @Override
        protected void updateItem(ToBuyItem item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new ToBuyItemCard(item, getIndex() + 1).getRoot());
            }
        }
    }

}