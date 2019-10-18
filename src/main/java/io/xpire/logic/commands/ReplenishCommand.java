package io.xpire.logic.commands;

import io.xpire.commons.core.Messages;
import io.xpire.commons.core.index.Index;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.Model;
import io.xpire.model.ToBuyItem;
import io.xpire.model.item.Item;
import io.xpire.model.item.Name;
import io.xpire.model.tag.Tag;

import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class ReplenishCommand extends Command {

    private final Index targetIndex;
    public static final String COMMAND_WORD = "replenish";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Moves the item identified by the index number to Replenish List.\n"
            + "Format: replenish|<index> (index must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + "|1" + "\n";
    private static final String MESSAGE_SUCCESS = "%s is moved to the Replenish List";
    public static final String MESSAGE_DUPLICATE_ITEM = "This item already exists in the Replenish List";
    private ToBuyItem toBuyItem;

    public ReplenishCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
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
        Set<Tag> tags = item.getTags();
        return new ToBuyItem(itemName, tags);
    }

}
