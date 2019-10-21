package io.xpire.ui;

import io.xpire.commons.util.DateUtil;
import io.xpire.model.item.Item;
import io.xpire.model.item.ReminderDate;
import io.xpire.model.item.ToBuyItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class ToBuyItemCard  extends UiPart<Region> {

    private static final String FXML = "ToBuyItemCard.fxml";

    public final ToBuyItem toBuyItem;

    @javafx.fxml.FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private FlowPane tags;

    public ToBuyItemCard(ToBuyItem item, int displayedIndex) {
        super(FXML);
        this.toBuyItem = item;
        this.id.setText(displayedIndex + ". ");
        this.name.setText(item.getName().toString());
        this.toBuyItem.getTags()
                .forEach(tag -> this.tags.getChildren().add(new Label(tag.getTagName())));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ItemCard)) {
            return false;
        }

        // state check
        ToBuyItemCard card = (ToBuyItemCard) other;
        return id.getText().equals(card.id.getText())
                && toBuyItem.equals(card.toBuyItem);
    }
}
