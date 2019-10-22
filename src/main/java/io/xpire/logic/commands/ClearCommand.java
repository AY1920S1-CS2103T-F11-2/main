package io.xpire.logic.commands;

import static io.xpire.commons.core.Messages.MESSAGE_VIEW_MODE;
import static java.util.Objects.requireNonNull;

import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.model.Model;
import io.xpire.model.Xpire;

/**
 * Clears all items in the list.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Item list has been cleared!";

    @Override
    public CommandResult execute(Model model, boolean isReplenishView) throws CommandException {
        if (isReplenishView) {
            throw new CommandException(MESSAGE_VIEW_MODE);
        } else {
            return execute(model);
        }
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setXpire(new Xpire());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
