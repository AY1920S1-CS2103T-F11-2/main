package io.xpire.logic.commands;

import io.xpire.commons.core.Messages;
import io.xpire.commons.core.index.Index;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.Model;
import io.xpire.model.ToBuyItem;
import io.xpire.model.item.Item;
import io.xpire.model.item.Name;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class ReplenishCommand extends Command {

    private final Index targetIndex;

    ReplenishCommand(Index targetIndex) {
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

        return null;
    }

    private ToBuyItem adaptItemToToBuy(Item item) {
        Name itemName =
    }

}
