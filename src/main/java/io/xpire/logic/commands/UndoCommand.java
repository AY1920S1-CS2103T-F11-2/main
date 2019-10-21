package io.xpire.logic.commands;

import static java.util.Objects.requireNonNull;

import io.xpire.model.Model;

/**
 * Undo the previous command
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo command: %s";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undo the previous command.\n"
            + "Example: " + COMMAND_WORD + "\n";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof UndoCommand;
        }
    }
}
