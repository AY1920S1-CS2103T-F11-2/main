package io.xpire.logic.commands;

import io.xpire.model.Model;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting Xpire as requested ...";

    @Override
    public CommandResult execute(Model model, boolean isReplenishView) {
        return isReplenishView ?
                new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true, true)
                : execute(model);
    }

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true, false);
    }

}
