package io.xpire.logic.commands;

import static io.xpire.logic.commands.CheckCommand.MESSAGE_SUCCESS;
import static io.xpire.logic.commands.CommandTestUtil.assertCommandSuccess;
import static io.xpire.testutil.TypicalItems.EXPIRED_APPLE;
import static io.xpire.testutil.TypicalItems.EXPIRED_MILK;
import static io.xpire.testutil.TypicalItems.EXPIRED_ORANGE;
import static io.xpire.testutil.TypicalItems.EXPIRING_FISH;
import static io.xpire.testutil.TypicalItems.getTypicalExpiryDateTracker;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import io.xpire.model.XpireModel;
import org.junit.jupiter.api.Test;

import io.xpire.model.XpireModelManager;
import io.xpire.model.UserPrefs;
import io.xpire.model.item.ExpiringSoonPredicate;
import io.xpire.model.item.ReminderThresholdExceededPredicate;

/**
 * Contains integration tests (interaction with the XpireModel, UndoCommand and RedoCommand) and unit tests for
 * {@code CheckCommand}.
 */
public class CheckCommandTest {

    private XpireModel xpireModel = new XpireModelManager(getTypicalExpiryDateTracker(), new UserPrefs());
    private XpireModel expectedXpireModel = new XpireModelManager(getTypicalExpiryDateTracker(), new UserPrefs());

    @Test
    public void execute_checkReminder_success() {
        String expectedMessage = MESSAGE_SUCCESS;
        ReminderThresholdExceededPredicate predicate = new ReminderThresholdExceededPredicate();
        CheckCommand command = new CheckCommand(predicate);
        expectedXpireModel.updateFilteredItemList(predicate);
        assertCommandSuccess(command, xpireModel, expectedMessage, expectedXpireModel);
        assertEquals(Arrays.asList(EXPIRED_APPLE, EXPIRING_FISH, EXPIRED_MILK, EXPIRED_ORANGE),
                xpireModel.getFilteredItemList());
    }

    @Test
    public void execute_checkDays_success() {
        String expectedMessage = MESSAGE_SUCCESS;
        ExpiringSoonPredicate predicate = new ExpiringSoonPredicate(5);
        CheckCommand command = new CheckCommand(predicate);
        expectedXpireModel.updateFilteredItemList(predicate);
        assertCommandSuccess(command, xpireModel, expectedMessage, expectedXpireModel);
        assertEquals(Arrays.asList(EXPIRED_APPLE, EXPIRED_MILK, EXPIRED_ORANGE), xpireModel.getFilteredItemList());
    }

    @Test
    public void equals() {
        CheckCommand checkReminderCommand = new CheckCommand(new ReminderThresholdExceededPredicate());
        CheckCommand checkDaysCommand = new CheckCommand(new ExpiringSoonPredicate(5));

        // same object -> returns true
        assertTrue(checkReminderCommand.equals(checkReminderCommand));

        // same values -> returns true
        CheckCommand checkDaysCommandCopy = new CheckCommand(new ExpiringSoonPredicate(5));
        assertTrue(checkDaysCommand.equals(checkDaysCommandCopy));

        // different types -> returns false
        assertFalse(checkReminderCommand.equals(1));

        // null -> returns false
        assertFalse(checkReminderCommand.equals(null));

        // different item -> returns false
        assertFalse(checkReminderCommand.equals(checkDaysCommand));
    }
}
