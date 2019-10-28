package io.xpire.logic.commands;

import static io.xpire.logic.CommandParserItemUtil.VALID_EXPIRY_DATE_BANANA;
import static io.xpire.logic.CommandParserItemUtil.VALID_EXPIRY_DATE_DUCK;
import static io.xpire.logic.CommandParserItemUtil.VALID_EXPIRY_DATE_EXPIRED_MILK;
import static io.xpire.logic.CommandParserItemUtil.VALID_EXPIRY_DATE_JELLY;
import static io.xpire.logic.CommandParserItemUtil.VALID_NAME_BANANA;
import static io.xpire.logic.CommandParserItemUtil.VALID_NAME_DUCK;
import static io.xpire.logic.CommandParserItemUtil.VALID_NAME_EXPIRED_MILK;
import static io.xpire.logic.CommandParserItemUtil.VALID_NAME_JELLY;
import static io.xpire.logic.CommandParserItemUtil.VALID_QUANTITY_JELLY;
import static io.xpire.logic.CommandParserItemUtil.VALID_REMINDER_THRESHOLD_BANANA;
import static io.xpire.logic.CommandParserItemUtil.VALID_REMINDER_THRESHOLD_JELLY;
import static io.xpire.logic.CommandParserItemUtil.VALID_TAG_DRINK;
import static io.xpire.logic.CommandParserItemUtil.VALID_TAG_FRIDGE;
import static io.xpire.logic.CommandParserItemUtil.VALID_TAG_FRUIT;
import static io.xpire.logic.CommandParserItemUtil.VALID_TAG_PROTEIN;
import static io.xpire.logic.commands.CommandTestUtil.assertCommandFailure;
import static io.xpire.logic.commands.CommandTestUtil.assertCommandSuccess;
import static io.xpire.logic.commands.CommandTestUtil.showItemAtIndex;

import static io.xpire.testutil.TypicalIndexes.INDEX_FIFTH_ITEM;

import static io.xpire.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static io.xpire.testutil.TypicalIndexes.INDEX_SECOND_ITEM;
import static io.xpire.testutil.TypicalIndexes.INDEX_SEVENTH_ITEM;
import static io.xpire.testutil.TypicalIndexes.INDEX_SIXTH_ITEM;
import static io.xpire.testutil.TypicalIndexes.INDEX_THIRD_ITEM;
import static io.xpire.testutil.TypicalItems.getTypicalExpiryDateTracker;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.TreeSet;

import io.xpire.model.item.XpireItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.xpire.commons.core.Messages;
import io.xpire.commons.core.index.Index;

import io.xpire.model.Model;
import io.xpire.model.ModelManager;
import io.xpire.model.UserPrefs;
import io.xpire.model.item.Quantity;
import io.xpire.model.tag.Tag;
import io.xpire.model.tag.TagComparator;

import io.xpire.testutil.ItemBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalExpiryDateTracker(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        XpireItem xpireItemToDelete = model.getFilteredXpireItemList().get(INDEX_FIRST_ITEM.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_ITEM);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_ITEM_SUCCESS, xpireItemToDelete);

        ModelManager expectedModel = new ModelManager(model.getXpire(), new UserPrefs());
        expectedModel.deleteItem(xpireItemToDelete);
        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredXpireItemList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showItemAtIndex(model, INDEX_FIRST_ITEM);

        XpireItem xpireItemToDelete = model.getFilteredXpireItemList().get(INDEX_FIRST_ITEM.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_ITEM);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_ITEM_SUCCESS, xpireItemToDelete);

        Model expectedModel = new ModelManager(model.getXpire(), new UserPrefs());
        expectedModel.deleteItem(xpireItemToDelete);
        showNoItem(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showItemAtIndex(model, INDEX_FIRST_ITEM);

        Index outOfBoundIndex = INDEX_SECOND_ITEM;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getXpire()[0].getItemList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
    }

    //test to delete tags for xpireItem with tags
    @Test
    public void execute_deleteTagsFromItemNotAllFields_success() {
        XpireItem targetXpireItem = model.getFilteredXpireItemList().get(INDEX_THIRD_ITEM.getZeroBased());
        Set<Tag> set = new TreeSet<>(new TagComparator());
        set.add(new Tag(VALID_TAG_FRIDGE));
        set.add(new Tag(VALID_TAG_PROTEIN));
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_THIRD_ITEM, set);
        ModelManager expectedModel = new ModelManager(model.getXpire(), new UserPrefs());
        XpireItem expectedXpireItem = new ItemBuilder().withName(VALID_NAME_DUCK)
                                             .withExpiryDate(VALID_EXPIRY_DATE_DUCK)
                                             .build();
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TAGS_SUCCESS, expectedXpireItem);
        expectedModel.setItem(targetXpireItem, expectedXpireItem); //set target xpireItem with no tags
        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    //Tags don't exist for you to delete.
    @Test
    public void execute_deleteTagsFromItemNotAllFields_throwsCommandException() {
        Set<Tag> set = new TreeSet<>(new TagComparator());
        set.add(new Tag(VALID_TAG_DRINK));
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_SIXTH_ITEM, set);
        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_TAGS);
    }

    //test to delete tags for xpireItem with all fields present
    @Test
    public void execute_deleteTagsFromItemAllFields_success() {
        XpireItem targetXpireItem = model.getFilteredXpireItemList().get(INDEX_FIFTH_ITEM.getZeroBased());
        Set<Tag> set = new TreeSet<>(new TagComparator());
        set.add(new Tag(VALID_TAG_FRIDGE));

        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIFTH_ITEM, set);
        ModelManager expectedModel = new ModelManager(model.getXpire(), new UserPrefs());

        XpireItem expectedXpireItem = new ItemBuilder().withName(VALID_NAME_JELLY)
                                             .withExpiryDate(VALID_EXPIRY_DATE_JELLY)
                                             .withQuantity(VALID_QUANTITY_JELLY)
                                             .withReminderThreshold(VALID_REMINDER_THRESHOLD_JELLY)
                                             .build();
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TAGS_SUCCESS, expectedXpireItem);
        expectedModel.setItem(targetXpireItem, expectedXpireItem); //set target xpireItem with no tags
        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    //test that does not delete any tags due to empty set
    @Test
    public void execute_deleteNoTagsFromItemAllFields_success() {
        XpireItem targetXpireItem = model.getFilteredXpireItemList().get(INDEX_FIFTH_ITEM.getZeroBased());
        Set<Tag> set = new TreeSet<>(new TagComparator());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIFTH_ITEM, set);
        ModelManager expectedModel = new ModelManager(model.getXpire(), new UserPrefs());
        XpireItem expectedXpireItem = new ItemBuilder().withName(VALID_NAME_JELLY)
                                             .withExpiryDate(VALID_EXPIRY_DATE_JELLY)
                                             .withQuantity(VALID_QUANTITY_JELLY)
                                             .withTags(VALID_TAG_FRIDGE)
                                             .withReminderThreshold(VALID_REMINDER_THRESHOLD_JELLY)
                                             .build();
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TAGS_SUCCESS, expectedXpireItem);
        expectedModel.setItem(targetXpireItem, expectedXpireItem); //set target xpireItem with no tags
        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteTagsFromItemAllFields_throwsCommandException() {
        Set<Tag> set = new TreeSet<>(new TagComparator());
        set.add(new Tag(VALID_TAG_FRUIT));
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_SEVENTH_ITEM, set);
        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_TAGS);
    }

    @Test
    public void execute_deleteQuantityLessThanItemQuantityFromItem_success() {
        //All xpireItem fields present
        XpireItem targetXpireItem = model.getFilteredXpireItemList().get(INDEX_SECOND_ITEM.getZeroBased());
        Quantity quantityToDeduct = new Quantity("2");
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_SECOND_ITEM, quantityToDeduct);
        ModelManager expectedModel = new ModelManager(model.getXpire(), new UserPrefs());
        XpireItem expectedXpireItem = new ItemBuilder().withName(VALID_NAME_BANANA)
                .withExpiryDate(VALID_EXPIRY_DATE_BANANA)
                .withQuantity("3")
                .withReminderThreshold(VALID_REMINDER_THRESHOLD_BANANA)
                .build();
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_QUANTITY_SUCCESS,
                quantityToDeduct.toString(), targetXpireItem);
        expectedModel.setItem(targetXpireItem, expectedXpireItem);
        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);

        //Not all xpireItem fields present
        targetXpireItem = model.getFilteredXpireItemList().get(INDEX_SIXTH_ITEM.getZeroBased());
        quantityToDeduct = new Quantity("1");
        deleteCommand = new DeleteCommand(INDEX_SIXTH_ITEM, quantityToDeduct);
        expectedModel = new ModelManager(model.getXpire(), new UserPrefs());
        expectedXpireItem = new ItemBuilder().withName(VALID_NAME_EXPIRED_MILK)
                .withExpiryDate(VALID_EXPIRY_DATE_EXPIRED_MILK)
                .withQuantity("1")
                .build();
        expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_QUANTITY_SUCCESS,
                quantityToDeduct.toString(), targetXpireItem);
        expectedModel.setItem(targetXpireItem, expectedXpireItem);
        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_deleteQuantityEqualsToItemQuantityFromItem_success() {
        Quantity quantityToDeduct = new Quantity("1");
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_THIRD_ITEM, quantityToDeduct);
        XpireItem xpireItemToDelete = model.getFilteredXpireItemList().get(INDEX_THIRD_ITEM.getZeroBased());
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_QUANTITY_SUCCESS,
                quantityToDeduct.toString(), xpireItemToDelete);
        Model expectedModel = new ModelManager(model.getXpire(), new UserPrefs());
        expectedModel.deleteItem(xpireItemToDelete);
        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteQuantityMoreThanItemQuantityFromItem_throwsCommandException() {
        XpireItem xpireItemToDelete = model.getFilteredXpireItemList().get(INDEX_THIRD_ITEM.getZeroBased());
        Quantity quantityToDeduct = new Quantity("3");
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_THIRD_ITEM, quantityToDeduct);
        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_DELETE_QUANTITY_FAILURE);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_ITEM);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_ITEM);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_ITEM);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different xpireItem -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoItem(Model model) {
        model.updateFilteredItemList(p -> false);

        assertTrue(model.getFilteredXpireItemList().isEmpty());
    }
}
