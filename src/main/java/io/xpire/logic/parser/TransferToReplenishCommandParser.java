package io.xpire.logic.parser;

import io.xpire.commons.core.Messages;
import io.xpire.commons.core.index.Index;

import io.xpire.logic.commands.TransferToReplenishCommand;
import io.xpire.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new TransferToReplenishCommand object
 */
public class TransferToReplenishCommandParser implements Parser<TransferToReplenishCommand> {

    @Override
    public TransferToReplenishCommand parse(String userInput) throws ParseException {
        String[] splitArgs = userInput.split("\\|", 1);
        if (splitArgs.length > 1) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    TransferToReplenishCommand.MESSAGE_USAGE));
        }
        Index index;
        try {
            index = ParserUtil.parseIndex(splitArgs[0]);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, TransferToReplenishCommand.MESSAGE_USAGE), pe);
        }
        return new TransferToReplenishCommand(index);
    }
}
