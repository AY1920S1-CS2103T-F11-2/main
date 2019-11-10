package io.xpire.logic.commands;

import static io.xpire.commons.util.CollectionUtil.requireAllNonNull;
import static io.xpire.logic.commands.util.CommandUtil.MESSAGE_REPLENISH_SHIFT_SUCCESS;

import java.util.Set;
import java.util.TreeSet;

import io.xpire.commons.core.Messages;
import io.xpire.commons.core.index.Index;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.logic.commands.util.CommandUtil;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.ListType;
import io.xpire.model.Model;
import io.xpire.model.item.Item;
import io.xpire.model.item.Quantity;
import io.xpire.model.item.XpireItem;
import io.xpire.model.state.ModifiedState;
import io.xpire.model.state.StateManager;
import io.xpire.model.tag.Tag;
import io.xpire.model.tag.TagComparator;
import javafx.collections.ObservableList;

/**
 * Deletes an xpireItem identified with its displayed index or tag(s) associated with the xpireItem.
 */
public class DeleteCommand extends Command {

    /**
     * Private enum to indicate whether command is deleting xpireItem, quantity or tags.
     */
    private enum DeleteMode { ITEM, QUANTITY, TAGS }

    public static final String COMMAND_WORD = "delete";
    public static final String COMMAND_SHORTHAND = "d";
    public static final String MESSAGE_UNKNOWN_DELETE_MODE = "Unknown Delete mode.";

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

    public static final String MESSAGE_DELETE_ITEM_SUCCESS = "Deleted item: %s";
    public static final String MESSAGE_DELETE_TAGS_SUCCESS = "Deleted tags from item: %s";
    public static final String MESSAGE_DELETE_QUANTITY_SUCCESS = "Reduced quantity by %s from item: %s";

    private final Index targetIndex;
    private final Set<Tag> tagSet;
    private final Quantity quantity;
    private final DeleteMode mode;
    private final ListType listType;
    private Item item;
    private String result = "";

    public DeleteCommand(ListType listType, Index targetIndex) {
        this.listType = listType;
        this.targetIndex = targetIndex;
        this.tagSet = null;
        this.quantity = null;
        this.mode = DeleteMode.ITEM;
    }

    public DeleteCommand(ListType listType, Index targetIndex, Set<Tag> tagSet) {
        this.listType = listType;
        this.targetIndex = targetIndex;
        this.tagSet = tagSet;
        this.quantity = null;
        this.mode = DeleteMode.TAGS;
    }

    public DeleteCommand(ListType listType, Index targetIndex, Quantity quantity) {
        this.listType = listType;
        this.targetIndex = targetIndex;
        this.tagSet = null;
        this.quantity = quantity;
        this.mode = DeleteMode.QUANTITY;
    }

    @Override
    public CommandResult execute(Model model, StateManager stateManager) throws CommandException, ParseException {
        requireAllNonNull(model, stateManager);
        this.requireNonEmptyCurrentList(model);
        stateManager.saveState(new ModifiedState(model));
        ObservableList<? extends Item> currentList = model.getCurrentList();

        if (this.targetIndex.getZeroBased() >= currentList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_INDEX);
        }

        Item targetItem = currentList.get(this.targetIndex.getZeroBased());
        this.item = targetItem;

        switch(this.mode) {
        case ITEM:
            return executeDeleteItem(model, targetItem);
        case TAGS:
            return executeDeleteTags(model, targetItem);
        case QUANTITY:
            return executeDeleteQuantity(model, targetItem);
        default:
            throw new CommandException(MESSAGE_UNKNOWN_DELETE_MODE);
        }
    }

    /**
     * Executes the command and returns the result message.
     *
     * @param model model {@code Model} which the command should operate on.
     * @param targetItem target item to reduce the quantity of.
     * @return feedback message of the operation result for display.
     * @throws CommandException If an error occurs during command execution.
     */
    private CommandResult executeDeleteQuantity(Model model, Item targetItem) throws CommandException, ParseException {
        assert this.quantity != null;
        XpireItem updatedItem = CommandUtil.reduceItemQuantity((XpireItem) targetItem, this.quantity);
        model.setItem(listType, targetItem, updatedItem);
        // transfer item to replenish list
        if (Quantity.quantityIsZero(updatedItem.getQuantity())) {
            CommandUtil.shiftItemToReplenishList(model, updatedItem);
            this.result = String.format(MESSAGE_DELETE_QUANTITY_SUCCESS, quantity.toString(), targetItem)
                    + "\n" + String.format(MESSAGE_REPLENISH_SHIFT_SUCCESS, updatedItem.getName());
            setShowInHistory(true);
            return new CommandResult(this.result);
        }
        this.result = String.format(MESSAGE_DELETE_QUANTITY_SUCCESS, quantity.toString(), targetItem);
        setShowInHistory(true);
        return new CommandResult(this.result);
    }

    /**
     * Executes the command and returns the result message.
     *
     * @param model model {@code Model} which the command should operate on.
     * @param targetItem target item to delete tags from.
     * @return feedback message of the operation result for display.
     * @throws CommandException If an error occurs during command execution.
     */
    private CommandResult executeDeleteTags(Model model, Item targetItem) throws CommandException {
        Item newTaggedItem;
        assert this.tagSet != null;
        newTaggedItem = removeTagsFromItem(targetItem, this.tagSet);
        model.setItem(listType, targetItem, newTaggedItem);
        this.result = String.format(MESSAGE_DELETE_TAGS_SUCCESS, newTaggedItem);
        setShowInHistory(true);
        return new CommandResult(this.result);
    }

    /**
     * Executes the command and returns the result message.
     *
     * @param model model {@code Model} which the command should operate on.
     * @param targetItem target item to delete completely.
     * @return feedback message of the operation result for display.
     */
    private CommandResult executeDeleteItem(Model model, Item targetItem) {
        model.deleteItem(listType, targetItem);
        this.result = String.format(MESSAGE_DELETE_ITEM_SUCCESS, targetItem);
        setShowInHistory(true);
        return new CommandResult(this.result);
    }

    /**
     * Removes Tag(s) from target replenishItem.
     *
     * @param targetItem The specified replenishItem that tags are to be removed.
     * @param tagSet Set of tags to remove.
     * @return item copy with removed tags.
     */
    private Item removeTagsFromItem(Item targetItem, Set<Tag> tagSet) throws CommandException {
        Item targetItemCopy;
        if (targetItem instanceof XpireItem) {
            targetItemCopy = new XpireItem((XpireItem) targetItem);
        } else {
            targetItemCopy = new Item(targetItem);
        }
        Set<Tag> originalTags = targetItemCopy.getTags();
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
        return "Delete command";
    }
}
