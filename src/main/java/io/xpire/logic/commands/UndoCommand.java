package io.xpire.logic.commands;

import static java.util.Objects.requireNonNull;

import io.xpire.commons.core.Messages;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.model.Model;
import io.xpire.model.state.StackManager;
import io.xpire.model.state.State;

/**
 * Undo the previous command
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo command: %s";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undo the previous command.\n"
            + "Example: " + COMMAND_WORD + "\n";

    @Override
    public CommandResult execute(Model model, StackManager stackManager) throws CommandException {
        requireNonNull(model);
        if (stackManager.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_NO_PREVIOUS_STATE);
        } else {
            State prevState = stackManager.pop();
            model.updateModel(prevState);
            return new CommandResult(String.format(MESSAGE_SUCCESS, prevState.getCommand()));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof UndoCommand;
        }
    }

    @Override
    public String toString() {
        return "Test";
    }

}
