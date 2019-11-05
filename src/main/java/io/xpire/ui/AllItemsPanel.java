package io.xpire.ui;

import java.util.List;
import java.util.logging.Logger;

import io.xpire.commons.core.LogsCenter;
import io.xpire.model.item.Item;
import io.xpire.model.item.XpireItem;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;


/**
 * Panel containing the list of items.
 */
public class AllItemsPanel extends UiPart<VBox> {
    private static final String FXML = "AllItemsPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ViewPanel.class);

    private List<XpireItem> xpireItems;
    private List<Item> replenishList;
    private TreeItem<String> trackedItems = new TreeItem<String>("Tracked items");
    private TreeItem<String> toBuyItems = new TreeItem<String>("To-buy items");

    @FXML
    private TreeView<String> tree;
    @FXML
    private TreeCell<String> cell;

    public AllItemsPanel(List<XpireItem> xpireItemList, List<Item> replenishList) {
        super(FXML);
        this.xpireItems = xpireItemList;
        this.replenishList = replenishList;
        TreeItem<String> rootItem = new TreeItem<String>("All items");
        tree.setRoot(rootItem);
        rootItem.setExpanded(true);
        ObservableList<TreeItem<String>> children = rootItem.getChildren();
        children.add((TreeItem<String>) trackedItems);
        children.add((TreeItem<String>) toBuyItems);
        /*
        addXpireItems(xpireItems);
        update();
         */
        displayItems(xpireItemList, replenishList);
        setClick();
    }

    /**
     * Toggle expanded property of tree cell on click.
     */
    private void setClick() {
        tree.setCellFactory(tree -> {
            TreeCell<String> cell = new TreeCell<String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            };
            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    TreeItem<String> treeItem = cell.getTreeItem();
                    if (treeItem.isExpanded()) {
                        treeItem.setExpanded(false);
                    } else {
                        treeItem.setExpanded(true);
                    }
                }
            });
            return cell;
        });
    }

    /**
     * Renders items for the all items panel.
     */
    public void displayItems(List<XpireItem> xpireItemList, List<Item> replenishList) {
        trackedItems.getChildren().clear();
        toBuyItems.getChildren().clear();
        xpireItemList.forEach(item -> {
            String name = item.getName().toString();
            String expiryDate = item.getExpiryDate().toString();
            TreeItem<String> treeItem = new TreeItem<String>(name);
            TreeItem<String> treeSubItem = new TreeItem<String>(expiryDate);
            treeItem.getChildren().add(treeSubItem);
            trackedItems.getChildren().add(treeItem);
        });
        replenishList.forEach(item -> {
            String name = item.getName().toString();
            TreeItem<String> treeItem = new TreeItem<String>(name);
            toBuyItems.getChildren().add(treeItem);
        });

    }
}
