package io.xpire.logic.commands;

import static io.xpire.commons.core.Messages.MESSAGE_REPLENISH_SHIFT_SUCCESS;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.xpire.commons.core.Messages;
import io.xpire.commons.core.index.Index;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.Model;
import io.xpire.model.StackManager;
import io.xpire.model.item.Name;
import io.xpire.model.item.Quantity;
import io.xpire.model.item.XpireItem;
import io.xpire.model.state.State;
import io.xpire.model.tag.Tag;
import io.xpire.model.tag.TagComparator;

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

    public static final String MESSAGE_USAGE =
            "Three formats available for " + COMMAND_WORD + ":\n"
            + "1) Deletes the item identified by the index number.\n"
            + "Format: delete|<index> (index must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + "|1" + "\n"
            + "2) Deletes 1 or more tags specified from the item identified by the index number.\n"
            + "Format: delete|<index>|<tag>[<other tags>]...\n"
            + "Example: " + COMMAND_WORD + "|1" + "|#Fruit #Food"
            + "3) Reduces the quantity in the item identified by the index number. \n"
            + "Format: delete|<index>|<quantity> (quantity must be positive and less than item's quantity.\n";

    public static final String MESSAGE_DELETE_ITEM_SUCCESS = "Deleted item: %s";
    public static final String MESSAGE_DELETE_TAGS_SUCCESS = "Deleted tags from item: %s";
    public static final String MESSAGE_DELETE_QUANTITY_SUCCESS = "Reduced quantity by %s from item: %s";
    public static final String MESSAGE_DELETE_QUANTITY_FAILURE = "Invalid quantity specified. \n"
            + "Quantity must be positive and less than item's quantity.";

    private final Index targetIndex;
    private final Set<Tag> tagSet;
    private final Quantity quantity;
    private final DeleteMode mode;
    private XpireItem item = null;
    private String result = "";

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
    public CommandResult execute(Model model, StackManager stackManager) throws CommandException, ParseException {
        requireNonNull(model);
        stackManager.saveState(new State(model));
        List<XpireItem> lastShownList = model.getFilteredXpireItemList();

        if (this.targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
        }

        XpireItem targetXpireItem = lastShownList.get(this.targetIndex.getZeroBased());
        this.item = targetXpireItem;

        switch(this.mode) {
        case ITEM:
            model.deleteItem(targetXpireItem);
            this.result = String.format(MESSAGE_DELETE_ITEM_SUCCESS, targetXpireItem);
            setShowInHistory(true);
            return new CommandResult(this.result);
        case TAGS:
            assert this.tagSet != null;
            XpireItem newTaggedXpireItem = removeTagsFromItem(new XpireItem(targetXpireItem), this.tagSet);
            model.setItem(targetXpireItem, newTaggedXpireItem);
            this.result = String.format(MESSAGE_DELETE_TAGS_SUCCESS, newTaggedXpireItem);
            setShowInHistory(true);
            return new CommandResult(this.result);
        case QUANTITY:
            assert this.quantity != null;
            XpireItem newQuantityXpireItem = reduceItemQuantity(new XpireItem(targetXpireItem), this.quantity);
            Name itemName = newQuantityXpireItem.getName();
            model.setItem(targetXpireItem, newQuantityXpireItem);
            // transfer item to replenish list
            if (Quantity.quantityIsZero(newQuantityXpireItem.getQuantity())) {
                model.shiftItemToReplenishList(newQuantityXpireItem);
                this.result = String.format(MESSAGE_DELETE_QUANTITY_SUCCESS, quantity.toString(), targetXpireItem)
                        + "\n" + String.format(MESSAGE_REPLENISH_SHIFT_SUCCESS, itemName);
                setShowInHistory(true);
                return new CommandResult(this.result);
            }
            this.result = String.format(MESSAGE_DELETE_QUANTITY_SUCCESS, quantity.toString(), targetXpireItem);
            setShowInHistory(true);
            return new CommandResult(this.result);
        default:
            throw new CommandException(Messages.MESSAGE_UNKNOWN_DELETE_MODE);
        }
    }

    /**
     * Removes Tag(s) from target xpireItem.
     *
     * @param targetXpireItem The specified xpireItem that tags are to be removed.
     * @param tagSet Set of tags to remove.
     * @return Original xpireItem with removed tags.
     */
    private XpireItem removeTagsFromItem(XpireItem targetXpireItem, Set<Tag> tagSet) throws CommandException {
        Set<Tag> originalTags = targetXpireItem.getTags();
        Set<Tag> newTags = new TreeSet<>(new TagComparator());
        if (!originalTags.containsAll(tagSet)) {
            throw new CommandException(Messages.MESSAGE_INVALID_TAGS);
        }
        for (Tag tag: originalTags) {
            if (!tagSet.contains(tag)) {
                newTags.add(tag);
            }
        }
        targetXpireItem.setTags(newTags);
        return targetXpireItem;
    }

    /**
     * Reduces xpireItem's quantity by amount specified.
     *
     * @param targetXpireItem XpireItem which amount will be reduced.
     * @param reduceByQuantity Quantity to be reduced.
     * @return
     * @throws ParseException
     */
    private XpireItem reduceItemQuantity(XpireItem targetXpireItem, Quantity reduceByQuantity) throws CommandException,
                                                                                                      ParseException {
        Quantity originalQuantity = targetXpireItem.getQuantity();
        if (originalQuantity.isLessThan(reduceByQuantity)) {
            throw new CommandException(MESSAGE_DELETE_QUANTITY_FAILURE);
        }
        Quantity updatedQuantity = originalQuantity.deductQuantity(reduceByQuantity);
        targetXpireItem.setQuantity(updatedQuantity);
        return targetXpireItem;
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
        return "the following Delete command:\n" + this.result;
    }
}
