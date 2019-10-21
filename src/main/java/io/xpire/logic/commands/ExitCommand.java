package io.xpire.logic.commands;

import io.xpire.model.Model;
import io.xpire.model.state.StackManager;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting Xpire as requested ...";

    @Override
    public CommandResult execute(Model model, StackManager stackManager) {
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
    }

    @Override
    public String toString() {
        return "Test";
    }

}
