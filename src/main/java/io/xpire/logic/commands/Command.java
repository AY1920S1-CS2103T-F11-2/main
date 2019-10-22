package io.xpire.logic.commands;

import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display.
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model) throws CommandException, ParseException;

    /**
     *  Executes the command and returns the result message depending on the view mode.
     *
     * @param model {@code Model} which the command should operate on.
     * @param isReplenishView is true if the view mode is replenish list.
     * @return feedback message of the operation result for display.
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model, boolean isReplenishView) throws CommandException, ParseException;

}
