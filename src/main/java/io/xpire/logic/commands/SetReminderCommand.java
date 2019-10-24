package io.xpire.logic.commands;

import static io.xpire.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.util.List;

import io.xpire.commons.core.Messages;
import io.xpire.commons.core.index.Index;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.model.Model;
import io.xpire.model.item.Item;
import io.xpire.model.item.ReminderThreshold;

/**
 * Changes the reminder threshold for an item identified with its displayed index.
 */

public class SetReminderCommand extends Command {

    public static final String COMMAND_WORD = "set reminder";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the reminder threshold of the item "
            + "identified with its displayed index. "
            + "Existing threshold will be overwritten by the input.\n"
            + "Format: set reminder|<index>|<threshold> (both index and threshold must be positive numbers)\n"
            + "Example: " + COMMAND_WORD + "|1|7";

    public static final String MESSAGE_SUCCESS_SET = "Reminder for item %d has been set to %s day(s)"
            + " before expiry date";
    public static final String MESSAGE_SUCCESS_RESET = "Disabled reminder for item %d";

    private final Index index;
    private final ReminderThreshold threshold;
    private Item item = null;

    /**
     * @param index Index of the item in the list.
     * @param threshold New threshold.
     */
    public SetReminderCommand(Index index, ReminderThreshold threshold) {
        requireAllNonNull(index, threshold);

        this.index = index;
        this.threshold = threshold;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Item> lastShownList = model.getFilteredItemList();

        if (this.index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
        }

        Item itemToSetReminder = lastShownList.get(this.index.getZeroBased());
        Item editedItem = itemToSetReminder;
        this.item = editedItem;
        editedItem.setReminderThreshold(this.threshold);

        model.setItem(itemToSetReminder, editedItem);
        model.updateFilteredItemList(Model.PREDICATE_SHOW_ALL_ITEMS);
        return new CommandResult(this.threshold.getValue() > 0
                ? String.format(MESSAGE_SUCCESS_SET, this.index.getOneBased(), this.threshold)
                : String.format(MESSAGE_SUCCESS_RESET, this.index.getOneBased()));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof SetReminderCommand)) {
            return false;
        } else {
            SetReminderCommand other = (SetReminderCommand) obj;
            return this.index.equals(other.index) && this.threshold.equals(other.threshold);
        }
    }

    @Override
    public String toString() {
        return "SetReminder Command: " + this.item.getName() + " Set for " + this.threshold + " days";
    }
}
