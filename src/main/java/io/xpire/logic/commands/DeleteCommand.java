package io.xpire.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.xpire.commons.core.Messages;
import io.xpire.commons.core.index.Index;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.Model;
import io.xpire.model.item.Item;
import io.xpire.model.item.Quantity;
import io.xpire.model.tag.Tag;
import io.xpire.model.tag.TagComparator;

/**
 * Deletes an item identified with its displayed index or tag(s) associated with the item.
 */
public class DeleteCommand extends Command {

    /**
     * Private enum to indicate whether command is deleting item, quantity or tags.
     */
    private enum DeleteMode { ITEM, QUANTITY, TAGS }

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE =
            "Three formats available for " + COMMAND_WORD + ":\n"
            + "1) Deletes the item identified by the index number.\n"
            + "Format: delete|<index> (index must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + "|1" + "\n"
            + "2) Deletes all tags in the item identified by the index number.\n"
            + "Format: delete|<index>|<tag>[<other tags>]...\n"
            + "Example: " + COMMAND_WORD + "|1" + "|#Fruit #Food"
            + "3) Reduces the quantity in the item identified by the index number. \n"
            + "Format: delete|<index>|<quantity> (quantity must be positive and less than item's quantity.\n";

    public static final String MESSAGE_DELETE_ITEM_SUCCESS = "Deleted Item: %s";
    public static final String MESSAGE_DELETE_TAGS_SUCCESS = "Deleted tags from item: %s";
    public static final String MESSAGE_DELETE_TAGS_FAILURE = "Did not manage to delete any tags.\n"
            + "You have specified tag(s) that are not found in item: %s";
    public static final String MESSAGE_DELETE_QUANTITY_SUCCESS = "Reduced quantity by %s from item: %s";
    public static final String MESSAGE_DELETE_QUANTITY_FAILURE = "Invalid quantity specified. \n"
            + "Quantity must be positive and less than item's quantity.";
    public static final String MESSAGE_DELETE_FAILURE = "Did not manage to delete anything";

    private final Index targetIndex;
    private final Set<Tag> tagSet;
    private final Quantity quantity;
    private final DeleteMode mode;
    private Item item = null;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
        this.tagSet = null;
        this.quantity = null;
        this.mode = DeleteMode.ITEM;
    }

    public DeleteCommand(Index targetIndex, Set<Tag> tagSet) {
        this.targetIndex = targetIndex;
        this.tagSet = tagSet;
        this.quantity = null;
        this.mode = DeleteMode.TAGS;
    }

    public DeleteCommand(Index targetIndex, Quantity quantity) {
        this.targetIndex = targetIndex;
        this.tagSet = null;
        this.quantity = quantity;
        this.mode = DeleteMode.QUANTITY;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException, ParseException {
        requireNonNull(model);
        List<Item> lastShownList = model.getFilteredItemList();

        if (this.targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
        }

        Item targetItem = lastShownList.get(this.targetIndex.getZeroBased());
        this.item = targetItem;
        switch(this.mode) {
        case ITEM:
            model.deleteItem(targetItem);
            return new CommandResult(String.format(MESSAGE_DELETE_ITEM_SUCCESS, targetItem));
        case TAGS:
            assert this.tagSet != null;
            Item newTaggedItem = removeTagsFromItem(targetItem, this.tagSet);
            model.setItem(targetItem, newTaggedItem);
            return new CommandResult(String.format(MESSAGE_DELETE_TAGS_SUCCESS, targetItem));
        case QUANTITY:
            assert this.quantity != null;
            Item newQuantityItem = reduceItemQuantity(targetItem, quantity);
            model.setItem(targetItem, newQuantityItem);
            /* TODO: Transfer to To-Buy-List*/
            if (Quantity.quantityIsZero(newQuantityItem.getQuantity())) {
                model.deleteItem(targetItem);
            }
            return new CommandResult(
                    String.format(MESSAGE_DELETE_QUANTITY_SUCCESS, quantity.toString(), targetItem));
        default:
            throw new CommandException(Messages.MESSAGE_UNKNOWN_DELETE_MODE);
        }
    }

    /**
     * Removes Tag(s) from target item.
     *
     * @param targetItem The specified item that tags are to be removed.
     * @param tagSet Set of tags to remove.
     * @return Original item with removed tags.
     */
    private Item removeTagsFromItem(Item targetItem, Set<Tag> tagSet) throws CommandException {
        Set<Tag> originalTags = targetItem.getTags();
        Set<Tag> newTags = new TreeSet<>(new TagComparator());
        if (!originalTags.containsAll(tagSet)) {
            throw new CommandException(Messages.MESSAGE_INVALID_TAGS);
        }
        for (Tag tag: originalTags) {
            if (!tagSet.contains(tag)) {
                newTags.add(tag);
            }
        }
        targetItem.setTags(newTags);
        return targetItem;
    }

    /**
     * Reduces item's quantity by amount specified.
     *
     * @param targetItem Item which amount will be reduced.
     * @param reduceByQuantity Quantity to be reduced.
     * @return
     * @throws ParseException
     */
    private Item reduceItemQuantity(Item targetItem, Quantity reduceByQuantity) throws CommandException,
                                                                                       ParseException {
        Quantity originalQuantity = targetItem.getQuantity();
        if (originalQuantity.isLessThan(reduceByQuantity)) {
            throw new CommandException(MESSAGE_DELETE_QUANTITY_FAILURE);
        }
        Quantity updatedQuantity = originalQuantity.deductQuantity(reduceByQuantity);
        targetItem.setQuantity(updatedQuantity);
        return targetItem;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof DeleteCommand)) {
            return false;
        } else {
            DeleteCommand other = (DeleteCommand) obj;
            return this.targetIndex.equals(other.targetIndex)
                    && this.mode.equals(other.mode);
        }
    }

    @Override
    public int hashCode() {
        return this.targetIndex.hashCode();
    }

    @Override
    public String toString() {
        String result = "Delete Command: ";
        switch(this.mode) {
        case TAGS:
            return result + "Delete Tags from " + this.item.getName() + ": " + this.tagSet;
        case ITEM:
            return result + "Deleted Item: " + this.item;
        case QUANTITY:
            return result + "Deleted Quantity from " + this.item.getName() + ": " + this.quantity;
        default:
            return result + "Unknown Mode";
        }
    }
}
