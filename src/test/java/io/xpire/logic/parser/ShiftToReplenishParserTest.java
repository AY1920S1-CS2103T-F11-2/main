package io.xpire.logic.parser;

import static io.xpire.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static io.xpire.logic.parser.CommandParserTestUtil.assertParseFailure;
import static io.xpire.testutil.TypicalIndexes.INDEX_THIRD_ITEM;

import org.junit.jupiter.api.Test;

import io.xpire.logic.commands.ShiftToReplenishCommand;

public class ShiftToReplenishParserTest {

    private ShiftToReplenishCommandParser parser = new ShiftToReplenishCommandParser();

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // invalid arguments
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ShiftToReplenishCommand.MESSAGE_USAGE));

        //invalid trailing arguments
        assertParseFailure(parser, "1||||||1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ShiftToReplenishCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_allFieldsPresent_success() {
        CommandParserTestUtil.assertEqualsParseSuccess(parser, "3",
                new ShiftToReplenishCommand(INDEX_THIRD_ITEM));
    }

}
