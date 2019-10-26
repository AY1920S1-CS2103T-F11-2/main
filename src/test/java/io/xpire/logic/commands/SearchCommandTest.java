package io.xpire.logic.commands;

import static io.xpire.commons.core.Messages.MESSAGE_ITEMS_LISTED_OVERVIEW;
import static io.xpire.commons.core.Messages.MESSAGE_SUGGESTIONS;
import static io.xpire.logic.commands.CommandTestUtil.assertCommandSuccess;
import static io.xpire.testutil.TypicalItems.BANANA;
import static io.xpire.testutil.TypicalItems.DUCK;
import static io.xpire.testutil.TypicalItems.JELLY;
import static io.xpire.testutil.TypicalItems.getTypicalExpiryDateTracker;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import io.xpire.model.XpireModel;
import io.xpire.model.XpireModelManager;
import org.junit.jupiter.api.Test;

import io.xpire.model.UserPrefs;
import io.xpire.model.item.ContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the XpireModel) for {@code SearchCommand}.
 */
public class SearchCommandTest {
    private XpireModel xpireModel = new XpireModelManager(getTypicalExpiryDateTracker(), new UserPrefs());
    private XpireModel expectedXpireModel = new XpireModelManager(getTypicalExpiryDateTracker(), new UserPrefs());

    @Test
    public void equals() {
        ContainsKeywordsPredicate firstPredicate =
                new ContainsKeywordsPredicate(Collections.singletonList("first"));
        ContainsKeywordsPredicate secondPredicate =
                new ContainsKeywordsPredicate(Collections.singletonList("second"));

        SearchCommand findFirstCommand = new SearchCommand(firstPredicate);
        SearchCommand findSecondCommand = new SearchCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        SearchCommand findFirstCommandCopy = new SearchCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different item -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_noMatchingKeywords_noItemsFoundNoRecommendations() {
        String expectedMessage = String.format(MESSAGE_ITEMS_LISTED_OVERVIEW, 0);
        ContainsKeywordsPredicate predicate = preparePredicate("Pineapple|Pear|#Cold");
        SearchCommand command = new SearchCommand(predicate);
        expectedXpireModel.updateFilteredItemList(predicate);
        assertCommandSuccess(command, xpireModel, expectedMessage, expectedXpireModel);
        assertEquals(Collections.emptyList(), xpireModel.getFilteredItemList());
    }

    @Test
    public void execute_noMatchingKeywords_noItemsFoundWithRecommendations() {
        String expectedMessage = String.format(MESSAGE_ITEMS_LISTED_OVERVIEW, 0)
                + String.format(MESSAGE_SUGGESTIONS, "[Banana]");
        ContainsKeywordsPredicate predicate = preparePredicate("Banaan");
        SearchCommand command = new SearchCommand(predicate);
        expectedXpireModel.updateFilteredItemList(predicate);
        assertCommandSuccess(command, xpireModel, expectedMessage, expectedXpireModel);
        assertEquals(Collections.emptyList(), xpireModel.getFilteredItemList());
    }

    @Test
    public void execute_allMatchingKeywords_someItemsFoundNoRecommendations() {
        String expectedMessage = String.format(MESSAGE_ITEMS_LISTED_OVERVIEW, 2);
        ContainsKeywordsPredicate predicate = preparePredicate("Banaan|#Fridge");
        SearchCommand command = new SearchCommand(predicate);
        expectedXpireModel.updateFilteredItemList(predicate);
        assertCommandSuccess(command, xpireModel, expectedMessage, expectedXpireModel);
        assertEquals(Arrays.asList(DUCK, JELLY), xpireModel.getFilteredItemList());
    }

    @Test
    public void execute_allMatchingKeywords_someItemsFoundWithRecommendations() {
        String expectedMessage = String.format(MESSAGE_ITEMS_LISTED_OVERVIEW, 0)
                + String.format(MESSAGE_SUGGESTIONS, "[[Fridge]]")
                + String.format(MESSAGE_SUGGESTIONS, "[Banana]");
        ContainsKeywordsPredicate predicate = preparePredicate("Banaan|#Fridg");
        SearchCommand command = new SearchCommand(predicate);
        expectedXpireModel.updateFilteredItemList(predicate);
        assertCommandSuccess(command, xpireModel, expectedMessage, expectedXpireModel);
        assertEquals(Collections.emptyList(), xpireModel.getFilteredItemList());
    }

    @Test
    public void execute_allMatchingKeywords_multipleItemsFound() {
        String expectedMessage = String.format(MESSAGE_ITEMS_LISTED_OVERVIEW, 3);
        ContainsKeywordsPredicate predicate = preparePredicate("Banana|#Fridge");
        SearchCommand command = new SearchCommand(predicate);
        expectedXpireModel.updateFilteredItemList(predicate);
        assertCommandSuccess(command, xpireModel, expectedMessage, expectedXpireModel);
        assertEquals(Arrays.asList(BANANA, DUCK, JELLY), xpireModel.getFilteredItemList());
    }

    @Test
    public void execute_someMatchingKeywords_multipleItemsFound() {
        String expectedMessage = String.format(MESSAGE_ITEMS_LISTED_OVERVIEW, 2);
        ContainsKeywordsPredicate predicate = preparePredicate("Pineapple|Banana|#Protein|#Cold");
        SearchCommand command = new SearchCommand(predicate);
        expectedXpireModel.updateFilteredItemList(predicate);
        assertCommandSuccess(command, xpireModel, expectedMessage, expectedXpireModel);
        assertEquals(Arrays.asList(BANANA, DUCK), xpireModel.getFilteredItemList());
    }

    /**
     * Parses {@code userInput} into a {@code ContainsKeywordsPredicate}.
     */
    private ContainsKeywordsPredicate preparePredicate(String parsedUserInput) {
        return new ContainsKeywordsPredicate(Arrays.asList(parsedUserInput.split("\\|")));
    }
}
