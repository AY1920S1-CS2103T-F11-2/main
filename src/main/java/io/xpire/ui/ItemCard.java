package io.xpire.ui;

import java.util.Optional;

import io.xpire.commons.util.DateUtil;
import io.xpire.model.item.Item;
import io.xpire.model.item.ReminderDate;
import io.xpire.model.item.XpireItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * An UI component that displays information of a {@code XpireItem}.
 */
public class ItemCard extends UiPart<Region> {

    private static final String FXML = "ItemCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     */

    private XpireItem xpireItem;
    private Item replenishItem;

    @FXML
    private HBox cardPane;
    @FXML
    private AnchorPane box;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label expiryDate;
    @FXML
    private Label quantity;
    @FXML
    private FlowPane tags;
    @FXML
    private Label reminder;

    public ItemCard(XpireItem item, int displayedIndex) {
        super(FXML);
        this.xpireItem = item;
        this.id.setText(displayedIndex + ". ");
        this.name.setText(item.getName().toString());
        this.expiryDate.setText("Expiry date: " + item.getExpiryDate().toStringWithCountdown());
        this.quantity.setText("Quantity: " + item.getQuantity().toString());
        Optional<ReminderDate> reminderDate = DateUtil.getReminderDate(
                item.getExpiryDate().getDate(), item.getReminderThreshold().getValue());
        if (reminderDate.isPresent()) {
            this.reminder.setText("Remind me from: " + reminderDate.get().toString());
        } else {
            this.reminder.setVisible(false);
        }
        this.xpireItem.getTags()
                .forEach(tag -> this.tags.getChildren().add(new Label(tag.getTagName())));

        box.setOnMouseClicked(e -> box.requestFocus());
    }

    public ItemCard(Item replenishItem, int displayedIndex) {
        super(FXML);
        this.replenishItem = replenishItem;
        this.id.setText(displayedIndex + ". ");
        this.name.setText(replenishItem.getName().toString());
        this.expiryDate.setVisible(false);
        this.quantity.setVisible(false);
        this.reminder.setVisible(false);
        this.replenishItem.getTags()
                      .forEach(tag -> this.tags.getChildren().add(new Label(tag.getTagName())));
        box.setOnMouseClicked(e -> box.requestFocus());
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
        ItemCard card = (ItemCard) other;
        return id.getText().equals(card.id.getText())
                && xpireItem.equals(card.xpireItem);
    }
}
