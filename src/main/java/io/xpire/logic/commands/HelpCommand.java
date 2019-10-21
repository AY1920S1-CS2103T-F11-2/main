package io.xpire.logic.commands;

import static io.xpire.logic.commands.CommandType.XPIRE;

import io.xpire.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

    public final CommandType commandType = XPIRE;

//    @Override
//    public CommandType getCommandType() {
//        return commandType;
//    }

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
    }
}
