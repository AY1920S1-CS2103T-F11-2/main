package io.xpire.logic.parser;

import io.xpire.commons.core.index.Index;
import io.xpire.logic.commands.ReplaceCommand;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.item.ExpiryDate;
import io.xpire.model.item.Quantity;

import static io.xpire.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static io.xpire.model.item.Quantity.DEFAULT_QUANTITY;

public class ReplaceCommandParser implements Parser<ReplaceCommand> {

    public ReplaceCommand parse(String args) throws ParseException {
        String[] arguments = args.split("\\|", 2);
        Index index;
        if (!areArgumentsPresent(arguments)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReplaceCommand.MESSAGE_USAGE));
        }
        try {
            index = ParserUtil.parseIndex(arguments[0]);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReplaceCommand.MESSAGE_USAGE), pe);
        };
        ExpiryDate expiryDate = ParserUtil.parseExpiryDate(arguments[1]);
        Quantity quantity;
        if (hasQuantity(arguments)) {
            quantity = ParserUtil.parseQuantity(arguments[2]);
        } else {
            quantity = new Quantity(DEFAULT_QUANTITY);
        }
        return new ReplaceCommand(index, expiryDate, quantity);
    }

    private static boolean areArgumentsPresent(String...arguments) {
        return arguments.length >= 2;
    }

    private static boolean hasQuantity(String...arguments) {
        return arguments.length >= 3 && !arguments[2].equals("");
    }
}
