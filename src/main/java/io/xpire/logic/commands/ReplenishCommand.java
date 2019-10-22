package io.xpire.logic.commands;

import static io.xpire.commons.core.Messages.MESSAGE_VIEW_MODE;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.xpire.commons.core.Messages;
import io.xpire.commons.core.index.Index;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.Model;
import io.xpire.model.item.ToBuyItem;
import io.xpire.model.item.Item;
import io.xpire.model.item.Name;
import io.xpire.model.tag.Tag;
import io.xpire.model.tag.TagComparator;

/**
 * Adds a {@code ToBuyItem} to the Replenish List.
 */
public class ReplenishCommand extends Command {

    public static final String COMMAND_WORD = "replenish";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Moves the item identified by the index number to Replenish List.\n"
            + "Format: replenish|<index> (index must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + "|1" + "\n";
    public static final String MESSAGE_DUPLICATE_ITEM = "This item already exists in the Replenish List";
    public static final String MESSAGE_SUCCESS = "%s is moved to the Replenish List";
    private static final Tag EXPIRED_TAG = new Tag("Expired");

    private ToBuyItem toBuyItem;
    private final Index targetIndex;

    public ReplenishCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, boolean isReplenishView) throws CommandException {
        if (isReplenishView) {
            throw new CommandException(MESSAGE_VIEW_MODE);
        } else {
            return execute(model);
        }
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {

        requireNonNull(model);
        List<Item> lastShownList = model.getFilteredItemList();

        if (this.targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
        }

        Item targetItem = lastShownList.get(this.targetIndex.getZeroBased());
        ToBuyItem toBuyItem = adaptItemToToBuy(targetItem);
        this.toBuyItem = toBuyItem;

        if (model.hasToBuyItem(toBuyItem)) {
            throw new CommandException(MESSAGE_DUPLICATE_ITEM);
        }

        model.addToBuyItem(this.toBuyItem);
        return new CommandResult(String.format(MESSAGE_SUCCESS, targetItem.getName()));
    }

    private ToBuyItem adaptItemToToBuy(Item item) {
        Name itemName = item.getName();
        Set<Tag> originalTags = item.getTags();
        Set<Tag> newTags = new TreeSet<>(new TagComparator());
        for (Tag tag: originalTags) {
            if (!newTags.contains(tag) && !tag.equals(EXPIRED_TAG)) {
                newTags.add(tag);
            }
        }
        return new ToBuyItem(itemName, newTags);
    }
}
