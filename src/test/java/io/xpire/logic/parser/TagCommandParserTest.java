package io.xpire.logic.parser;

import static io.xpire.logic.parser.CommandParserTestUtil.assertEqualsParseSuccess;
import static io.xpire.logic.parser.CommandParserTestUtil.assertParseFailure;
import static io.xpire.model.ListType.XPIRE;
import static io.xpire.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static io.xpire.testutil.TypicalItemsFields.INVALID_TAG;
import static io.xpire.testutil.TypicalItemsFields.VALID_TAG_FRIDGE;
import static io.xpire.testutil.TypicalItemsFields.VALID_TAG_FRUIT;

import io.xpire.model.ListType;
import org.junit.jupiter.api.Test;

import io.xpire.commons.core.Messages;
import io.xpire.logic.commands.TagCommand;
import io.xpire.model.tag.Tag;

public class TagCommandParserTest {

    private TagCommandParser parser = new TagCommandParser(XPIRE);

    @Test
    public void parse_validSingleTag_returnTagCommand() {
        assertEqualsParseSuccess(parser, "1|#Fruit", new TagCommand(XPIRE, INDEX_FIRST_ITEM,
                new String[]{VALID_TAG_FRUIT}));
    }

    @Test
    public void parse_validMultipleTags_returnTagCommand() {
        assertEqualsParseSuccess(parser, "1|#Fruit#Fridge", new TagCommand(XPIRE, INDEX_FIRST_ITEM,
                new String[]{VALID_TAG_FRUIT, VALID_TAG_FRIDGE}));
    }

    @Test
    public void parse_invalidIndex_throwsParserException() {
        assertParseFailure(parser, "-1", String
                .format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_blankTag_throwsParserException() {
        assertParseFailure(parser, "2|#", String
                .format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTag_throwsParserException() {
        assertParseFailure(parser, "2|" + INVALID_TAG, String
                .format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTagWithValidTag_throwsParserException() {
        assertParseFailure(parser, "2|" + "#" + INVALID_TAG + "#" + VALID_TAG_FRUIT, Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_showTags_returnsShowCommand() {
        assertEqualsParseSuccess(parser, "", new TagCommand(XPIRE));
    }
}
