package io.xpire.logic.commands.util;

import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.model.Model;
import io.xpire.model.item.Item;
import io.xpire.model.item.Quantity;
import io.xpire.model.item.XpireItem;

import java.util.List;

import static io.xpire.commons.core.Messages.MESSAGE_DUPLICATE_ITEM_REPLENISH;
import static io.xpire.model.ListType.REPLENISH;
import static io.xpire.model.ListType.XPIRE;
import static java.util.Objects.requireNonNull;

public class CommandUtil {

    public static final String MESSAGE_INVALID_REDUCE_QUANTITY = "Invalid quantity specified. \n"
            + "Quantity must be positive and less than or equals to item's quantity.";

    public static final String MESSAGE_INVALID_INCREASE_QUANTITY =
            "Quantity specified would cause item quantity to exceed maximum limit. \n";

    /**
     * Updates item quantity of an existing item on the list.
     * @param model
     * @param existingItem
     * @return item with updated quantity.
     * @throws CommandException if the resulting quantity exceeds the maximum limit.
     */
    public static XpireItem updateItemQuantity(Model model, XpireItem existingItem) throws CommandException {
        XpireItem itemToReplace = retrieveXpireItemFromList(existingItem, model.getItemList(XPIRE));
        XpireItem itemWithUpdatedQuantity = increaseItemQuantity(itemToReplace, existingItem.getQuantity());
        model.setItem(XPIRE, itemToReplace, itemWithUpdatedQuantity);
        return itemWithUpdatedQuantity;
    }


    /**
     * Reduces xpireItem's quantity by amount specified.
     *
     * @param targetXpireItem XpireItem which amount will be reduced.
     * @param reduceByQuantity Quantity to be reduced.
     * @return The new XpireItem with its quantity reduced.
     * @throws CommandException if new item quantity is invalid.
     */
    public static XpireItem reduceItemQuantity(XpireItem targetXpireItem, Quantity reduceByQuantity) throws CommandException {
        XpireItem targetItemCopy = new XpireItem(targetXpireItem);
        Quantity originalQuantity = targetItemCopy.getQuantity();
        if (originalQuantity.isLessThan(reduceByQuantity)) {
            throw new CommandException(MESSAGE_INVALID_REDUCE_QUANTITY);
        }
        Quantity updatedQuantity = originalQuantity.deductQuantity(reduceByQuantity);
        targetItemCopy.setQuantity(updatedQuantity);
        return targetItemCopy;
    }

    /**
     * Increases the item quantity for any duplicate items.
     *
     * @param targetItem the target item to increase the quantity of.
     * @param quantity to increase by.
     * @return The new item with revised quantity.
     * @throws CommandException if the input quantity causes the new quantity to exceed the maximum limit.
     */
    public static XpireItem increaseItemQuantity(XpireItem targetItem, Quantity quantity) throws CommandException {
        XpireItem targetItemCopy = new XpireItem(targetItem);
        Quantity prevQuantity = targetItemCopy.getQuantity();
        if (prevQuantity.sumExceedsMaximumLimit(quantity)) {
            throw new CommandException(MESSAGE_INVALID_INCREASE_QUANTITY);
        }
        Quantity updatedQuantity = prevQuantity.increaseQuantity(quantity);
        targetItemCopy.setQuantity(updatedQuantity);
        return targetItemCopy;
    }

    /**
     * Retrieves item that is the same as item inputted by user.
     *
     * @param item existing in the tracking list.
     * @param list where item is retrieved from.
     * @return exact item which is the same as input item.
     **/
    public static XpireItem retrieveXpireItemFromList(XpireItem item, List<? extends Item> list) {
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
     * Shifts Item to ReplenishList.
     */
    public static void shiftItemToReplenishList(Model model, XpireItem itemToShift) throws CommandException {
        Item remodelledItem = itemToShift.remodel();
        if (model.hasItem(REPLENISH, remodelledItem)) {
            throw new CommandException(MESSAGE_DUPLICATE_ITEM_REPLENISH);
        }
        model.addItem(REPLENISH, remodelledItem);
        model.deleteItem(XPIRE, itemToShift);
    }
}
