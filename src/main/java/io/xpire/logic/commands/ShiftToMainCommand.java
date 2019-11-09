package io.xpire.logic.commands;

import static io.xpire.commons.core.Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX;
import static io.xpire.model.ListType.REPLENISH;
import static io.xpire.model.ListType.XPIRE;
import static java.util.Objects.requireNonNull;

import java.util.List;

import io.xpire.commons.core.index.Index;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.Model;
import io.xpire.model.item.ExpiryDate;
import io.xpire.model.item.Item;
import io.xpire.model.item.Quantity;
import io.xpire.model.item.XpireItem;
import io.xpire.model.state.ModifiedState;
import io.xpire.model.state.StateManager;

//@@author liawsy
/**
 * Shifts an {@Item} back to the main list.
 */
public class ShiftToMainCommand extends Command {
    public static final String COMMAND_WORD = "shift";
    public static final String COMMAND_SHORTHAND = "sh";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Moves the item identified by the index number to the main list.\n"
            + "Format: shift|<index>|<expiry date>[|<quantity>] (index must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + "|1|11/2/1997|3" + "\n";
    public static final String MESSAGE_SUCCESS_SHIFT = "%s is moved to the main list";
    public static final String MESSAGE_SUCCESS_UPDATE_QUANTITY =
            "Quantity for %s in the main list is increased to %s.\n" + "Original tags are retained. \n";

    private final Index targetIndex;
    private final ExpiryDate expiryDate;
    private final Quantity quantity;
    private String result;

    public ShiftToMainCommand(Index targetIndex, ExpiryDate expiryDate, Quantity quantity) {
        this.targetIndex = targetIndex;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.result = "";
    }

    @Override
    public CommandResult execute(Model model, StateManager stateManager) throws CommandException, ParseException {
        requireNonNull(model);
        stateManager.saveState(new ModifiedState(model));

        List<? extends Item> lastShownList = model.getCurrentList();
        if (this.targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
        }
        Item targetItem = lastShownList.get(this.targetIndex.getZeroBased());
        XpireItem remodelledItem = targetItem.remodel(this.expiryDate, this.quantity);
        if (model.hasItem(XPIRE, remodelledItem)) {
            XpireItem itemToReplace = retrieveXpireItem(remodelledItem, model.getItemList(XPIRE));
            XpireItem itemWithUpdatedQuantity = increaseItemQuantity(itemToReplace, this.quantity);
            model.setItem(XPIRE, itemToReplace, itemWithUpdatedQuantity);
            this.result = String.format(MESSAGE_SUCCESS_UPDATE_QUANTITY, remodelledItem.getName(),
                    itemWithUpdatedQuantity.getQuantity());
        } else {
            model.addItem(XPIRE, remodelledItem);
            model.deleteItem(REPLENISH, targetItem);
            this.result = String.format(MESSAGE_SUCCESS_SHIFT, remodelledItem.getName());
        }
        setShowInHistory(true);
        return new CommandResult(this.result);
    }

    @Override
    public String toString() {
        return "Shift command";
    }

    /**
     * Retrieves item that is the same as item inputted by user.
     *
     * @param item existing in the tracking list.
     * @param list where item is retrieved from.
     * @return exact item which is the same as input item.
     **/
    private XpireItem retrieveXpireItem(XpireItem item, List<? extends Item> list) {
        requireNonNull(item);
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSameItem(item)) {
                index = i;
            }
        }
        return (XpireItem) list.get(index);
    }

    /**
     * Increases the item quantity for any duplicate items.
     *
     * @param targetItem the target item to increase the quantity of.
     * @param quantity how much to increase the item quantity by.
     * @return The new item with revised quantity.
     * @throws ParseException if the input quantity results in the new quantity to exceed the maximum limit.
     */
    private XpireItem increaseItemQuantity(XpireItem targetItem, Quantity quantity) throws ParseException {
        XpireItem targetItemCopy = new XpireItem(targetItem);
        Quantity prevQuantity = targetItemCopy.getQuantity();
        Quantity updatedQuantity = prevQuantity.increaseQuantity(quantity);
        targetItemCopy.setQuantity(updatedQuantity);
        return targetItemCopy;
    }
}
