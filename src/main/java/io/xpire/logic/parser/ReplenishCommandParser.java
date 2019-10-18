package io.xpire.logic.parser;

import io.xpire.commons.core.Messages;
import io.xpire.commons.core.index.Index;

import io.xpire.logic.commands.ReplenishCommand;
import io.xpire.logic.parser.exceptions.ParseException;

public class ReplenishCommandParser implements Parser<ReplenishCommand> {

    @Override
    public ReplenishCommand parse(String userInput) throws ParseException {
        String[] splitArgs = userInput.split("\\|", 1);
        if (splitArgs.length > 1) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    ReplenishCommand.MESSAGE_USAGE));
        }
        Index index;
        try {
            index = ParserUtil.parseIndex(splitArgs[0]);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ReplenishCommand.MESSAGE_USAGE), pe);
        }
        return new ReplenishCommand(index);
    }
}
