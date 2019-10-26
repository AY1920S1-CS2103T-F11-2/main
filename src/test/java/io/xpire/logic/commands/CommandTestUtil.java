package io.xpire.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.xpire.commons.core.index.Index;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.XpireModel;
import io.xpire.model.ListView;
import io.xpire.model.item.ContainsKeywordsPredicate;
import io.xpire.model.item.XpireItem;
import io.xpire.testutil.Assert;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualXpireModel} matches {@code expectedXpireModel}
     */
    public static void assertCommandSuccess(Command command, XpireModel actualXpireModel, CommandResult expectedCommandResult,
                                            XpireModel expectedXpireModel) {
        try {
            CommandResult result = command.execute(actualXpireModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedXpireModel, actualXpireModel);
        } catch (CommandException | ParseException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, XpireModel, CommandResult, XpireModel)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, XpireModel actualXpireModel, String expectedMessage,
                                            XpireModel expectedXpireModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualXpireModel, expectedCommandResult, expectedXpireModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the expiry date tracker, filtered item list and selected item in {@code actualXpireModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, XpireModel actualXpireModel, String expectedMessage) {
        // we are unable to defensively copy the xpireModel for comparison later, so we can
        // only do so by copying its components.
        ListView expectedXpire = new ListView(actualXpireModel.getListView());
        List<XpireItem> expectedFilteredList = new ArrayList<>(actualXpireModel.getFilteredItemList());

        Assert.assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualXpireModel));
        assertEquals(expectedXpire, actualXpireModel.getListView());
        assertEquals(expectedFilteredList, actualXpireModel.getFilteredItemList());
    }
    /**
     * Updates {@code xpireModel}'s filtered list to show only the item at the given {@code targetIndex} in the
     * {@code xpireModel}'s expiry date tracker.
     */
    public static void showItemAtIndex(XpireModel xpireModel, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < xpireModel.getFilteredItemList().size());

        XpireItem xpireItem = xpireModel.getFilteredItemList().get(targetIndex.getZeroBased());
        final String[] splitName = xpireItem.getName().toString().split("\\s+");
        xpireModel.updateFilteredItemList(new ContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, xpireModel.getFilteredItemList().size());
    }

}
