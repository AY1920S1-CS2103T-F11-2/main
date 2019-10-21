package io.xpire.logic.parser;

import static io.xpire.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import io.xpire.logic.commands.UndoCommand;
import io.xpire.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UndoCommand object.
 */
public class UndoCommandParser implements Parser<UndoCommand> {
    @Override
    public UndoCommand parse(String userInput) throws ParseException {

        String args = userInput.replaceAll("\\|", "");
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            return new UndoCommand();
        } else {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UndoCommand.MESSAGE_USAGE));
        }
    }
}
