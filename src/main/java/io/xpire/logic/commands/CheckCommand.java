package io.xpire.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import io.xpire.model.Model;
import io.xpire.model.StackManager;
import io.xpire.model.item.ExpiringSoonPredicate;
import io.xpire.model.item.ReminderThresholdExceededPredicate;
import io.xpire.model.item.XpireItem;
import io.xpire.model.state.State;

/**
 * Displays all items whose expiry date falls within the specified duration (in days).
 */
public class CheckCommand extends Command {
    public static final String COMMAND_WORD = "check";
    public static final String COMMAND_SHORTHAND = "ch";

    public static final String MESSAGE_SUCCESS = "XpireItem(s) expiring soon";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Displays all items whose expiry date is within"
            + "the specified duration (in days). Expired items, if any, are also included in the list.\n"
            + "Format: check[|<days>] (days must be a non negative number)\n"
            + "Example: " + COMMAND_WORD + "|7\n"
            + "If no duration is specified, expired items and items whose days to expiry date are less than or equals "
            + "to the remainder threshold will be displayed.\n";

    private final Predicate<XpireItem> predicate;

    public CheckCommand(ExpiringSoonPredicate predicate) {
        this.predicate = predicate;
    }

    public CheckCommand(ReminderThresholdExceededPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, StackManager stackManager) {
        requireNonNull(model);
        State test = new State(model);
        stackManager.saveState(test);
        model.updateFilteredItemList(this.predicate);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof CheckCommand)) {
            return false;
        } else {
            CheckCommand other = (CheckCommand) obj;
            return this.predicate.equals(other.predicate);
        }
    }

    @Override
    public int hashCode() {
        return this.predicate.hashCode();
    }

    @Override
    public String toString() {
        return "Check Command";
    }
}
