package io.xpire.logic.commands;

import static io.xpire.commons.core.Messages.MESSAGE_VIEW_MODE;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;

import io.xpire.commons.core.Messages;
import io.xpire.commons.core.index.Index;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.model.Model;
import io.xpire.model.item.ExpiryDate;
import io.xpire.model.item.Item;
import io.xpire.model.item.Name;
import io.xpire.model.item.Quantity;
import io.xpire.model.item.ToBuyItem;
import io.xpire.model.tag.Tag;

public class ReplaceCommand extends Command {

    public static final String COMMAND_WORD = "replace";

    public static final String MESSAGE_USAGE_FORMAT = "Format: replace|"
            + "<item index in replenish list>|<expiry date>[|<quantity>]\n"
            + "Example: " + COMMAND_WORD + "|1|11/12/1999|2";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Moves an item in the replenish list to the tracker.\n"
            + MESSAGE_USAGE_FORMAT;

    public static final String MESSAGE_SUCCESS = "New item replenished: %s";

    public static final String MESSAGE_DUPLICATE_ITEM = "This item is already replenished";

    public static final String MESSAGE_TO_REPLENISH_VIEW = "Please change to replenish view";

    private final Index targetIndex;
    private final ExpiryDate expiryDate;
    private final Quantity quantity;

    public ReplaceCommand(Index targetIndex, ExpiryDate expiryDate, Quantity quantity) {
        this.targetIndex = targetIndex;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
    }

    @Override
    public CommandResult execute(Model model, boolean isReplenishView) throws CommandException {
        if (isReplenishView) {
            return execute(model);
        } else {
            throw new CommandException(MESSAGE_TO_REPLENISH_VIEW);
        }
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<ToBuyItem> lastShownToBuyList = model.getFilteredReplenishList();

        if (this.targetIndex.getZeroBased() >= lastShownToBuyList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
        }

        ToBuyItem targetItem = lastShownToBuyList.get(this.targetIndex.getZeroBased());
        Item toAdd = adaptToBuyToItem(targetItem, expiryDate, quantity);
        if (model.hasItem(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_ITEM);
        }
        model.addItem(toAdd);
        model.deleteToBuyItem(targetItem);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd.getName()));
    }

    private Item adaptToBuyToItem(ToBuyItem toBuy, ExpiryDate expiryDate, Quantity quantity) {
        Name itemName = toBuy.getName();
        Set<Tag> tags = toBuy.getTags();
        return new Item(itemName, expiryDate, quantity, tags);
    }

}
