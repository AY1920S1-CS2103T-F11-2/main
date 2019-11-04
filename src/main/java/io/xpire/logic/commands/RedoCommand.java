package io.xpire.logic.commands;

import static java.util.Objects.requireNonNull;

import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.model.Model;
import io.xpire.model.StackManager;
import io.xpire.model.state.State;

/**
 * Redo the previous Undo Command.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_REDO_SUCCESS = "Redo %s";
    public static final String MESSAGE_REDO_FAILURE = "There are no commands to redo.";

    @Override
    public CommandResult execute(Model model, StackManager stackManager) throws CommandException {
        requireNonNull(model);
        if (stackManager.isRedoStackEmpty()) {
            throw new CommandException(MESSAGE_REDO_FAILURE);
        }
        State succeedingState = stackManager.redo();
        model.update(succeedingState);
        return new CommandResult(MESSAGE_REDO_SUCCESS);
    }

    @Override
    public String toString() {
        return "Redo Command";
    }
}
