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

import io.xpire.model.XpireModel;
import io.xpire.model.XpireModelManager;
import io.xpire.model.item.XpireItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.xpire.commons.core.Messages;
import io.xpire.commons.core.index.Index;

import io.xpire.model.UserPrefs;
import io.xpire.model.item.Quantity;
import io.xpire.model.tag.Tag;
import io.xpire.model.tag.TagComparator;

import io.xpire.testutil.ItemBuilder;

/**
 * Contains integration tests (interaction with the XpireModel, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private XpireModel xpireModel;

    @BeforeEach
    public void setUp() {
        xpireModel = new XpireModelManager(getTypicalExpiryDateTracker(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        XpireItem xpireItemToDelete = xpireModel.getFilteredItemList().get(INDEX_FIRST_ITEM.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_ITEM);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_ITEM_SUCCESS, xpireItemToDelete);

        XpireModelManager expectedModel = new XpireModelManager(xpireModel.getListView(), new UserPrefs());
        expectedModel.deleteItem(xpireItemToDelete);
        assertCommandSuccess(deleteCommand, xpireModel, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(xpireModel.getFilteredItemList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, xpireModel, Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showItemAtIndex(xpireModel, INDEX_FIRST_ITEM);

        XpireItem xpireItemToDelete = xpireModel.getFilteredItemList().get(INDEX_FIRST_ITEM.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_ITEM);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_ITEM_SUCCESS, xpireItemToDelete);

        XpireModel expectedXpireModel = new XpireModelManager(xpireModel.getListView(), new UserPrefs());
        expectedXpireModel.deleteItem(xpireItemToDelete);
        showNoItem(expectedXpireModel);

        assertCommandSuccess(deleteCommand, xpireModel, expectedMessage, expectedXpireModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showItemAtIndex(xpireModel, INDEX_FIRST_ITEM);

        Index outOfBoundIndex = INDEX_SECOND_ITEM;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < xpireModel.getListView().getItemList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, xpireModel, Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
    }

    //test to delete tags for item with tags
    @Test
    public void execute_deleteTagsFromItemNotAllFields_success() {
        XpireItem targetXpireItem = xpireModel.getFilteredItemList().get(INDEX_THIRD_ITEM.getZeroBased());
        Set<Tag> set = new TreeSet<>(new TagComparator());
        set.add(new Tag(VALID_TAG_FRIDGE));
        set.add(new Tag(VALID_TAG_PROTEIN));
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_THIRD_ITEM, set);
        XpireModelManager expectedModel = new XpireModelManager(xpireModel.getListView(), new UserPrefs());
        XpireItem expectedXpireItem = new ItemBuilder().withName(VALID_NAME_DUCK)
                                             .withExpiryDate(VALID_EXPIRY_DATE_DUCK)
                                             .build();
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TAGS_SUCCESS, expectedXpireItem);
        expectedModel.setItem(targetXpireItem, expectedXpireItem); //set target item with no tags
        assertCommandSuccess(deleteCommand, xpireModel, expectedMessage, expectedModel);
    }

    //Tags don't exist for you to delete.
    @Test
    public void execute_deleteTagsFromItemNotAllFields_throwsCommandException() {
        Set<Tag> set = new TreeSet<>(new TagComparator());
        set.add(new Tag(VALID_TAG_DRINK));
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_SIXTH_ITEM, set);
        assertCommandFailure(deleteCommand, xpireModel, Messages.MESSAGE_INVALID_TAGS);
    }

    //test to delete tags for item with all fields present
    @Test
    public void execute_deleteTagsFromItemAllFields_success() {
        XpireItem targetXpireItem = xpireModel.getFilteredItemList().get(INDEX_FIFTH_ITEM.getZeroBased());
        Set<Tag> set = new TreeSet<>(new TagComparator());
        set.add(new Tag(VALID_TAG_FRIDGE));

        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIFTH_ITEM, set);
        XpireModelManager expectedModel = new XpireModelManager(xpireModel.getListView(), new UserPrefs());

        XpireItem expectedXpireItem = new ItemBuilder().withName(VALID_NAME_JELLY)
                                             .withExpiryDate(VALID_EXPIRY_DATE_JELLY)
                                             .withQuantity(VALID_QUANTITY_JELLY)
                                             .withReminderThreshold(VALID_REMINDER_THRESHOLD_JELLY)
                                             .build();
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TAGS_SUCCESS, expectedXpireItem);
        expectedModel.setItem(targetXpireItem, expectedXpireItem); //set target item with no tags
        assertCommandSuccess(deleteCommand, xpireModel, expectedMessage, expectedModel);
    }

    //test that does not delete any tags due to empty set
    @Test
    public void execute_deleteNoTagsFromItemAllFields_success() {
        XpireItem targetXpireItem = xpireModel.getFilteredItemList().get(INDEX_FIFTH_ITEM.getZeroBased());
        Set<Tag> set = new TreeSet<>(new TagComparator());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIFTH_ITEM, set);
        XpireModelManager expectedModel = new XpireModelManager(xpireModel.getListView(), new UserPrefs());
        XpireItem expectedXpireItem = new ItemBuilder().withName(VALID_NAME_JELLY)
                                             .withExpiryDate(VALID_EXPIRY_DATE_JELLY)
                                             .withQuantity(VALID_QUANTITY_JELLY)
                                             .withTags(VALID_TAG_FRIDGE)
                                             .withReminderThreshold(VALID_REMINDER_THRESHOLD_JELLY)
                                             .build();
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TAGS_SUCCESS, expectedXpireItem);
        expectedModel.setItem(targetXpireItem, expectedXpireItem); //set target item with no tags
        assertCommandSuccess(deleteCommand, xpireModel, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteTagsFromItemAllFields_throwsCommandException() {
        Set<Tag> set = new TreeSet<>(new TagComparator());
        set.add(new Tag(VALID_TAG_FRUIT));
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_SEVENTH_ITEM, set);
        assertCommandFailure(deleteCommand, xpireModel, Messages.MESSAGE_INVALID_TAGS);
    }

    @Test
    public void execute_deleteQuantityLessThanItemQuantityFromItem_success() {
        //All item fields present
        XpireItem targetXpireItem = xpireModel.getFilteredItemList().get(INDEX_SECOND_ITEM.getZeroBased());
        Quantity quantityToDeduct = new Quantity("2");
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_SECOND_ITEM, quantityToDeduct);
        XpireModelManager expectedModel = new XpireModelManager(xpireModel.getListView(), new UserPrefs());
        XpireItem expectedXpireItem = new ItemBuilder().withName(VALID_NAME_BANANA)
                .withExpiryDate(VALID_EXPIRY_DATE_BANANA)
                .withQuantity("3")
                .withReminderThreshold(VALID_REMINDER_THRESHOLD_BANANA)
                .build();
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_QUANTITY_SUCCESS,
                quantityToDeduct.toString(), targetXpireItem);
        expectedModel.setItem(targetXpireItem, expectedXpireItem);
        assertCommandSuccess(deleteCommand, xpireModel, expectedMessage, expectedModel);

        //Not all item fields present
        targetXpireItem = xpireModel.getFilteredItemList().get(INDEX_SIXTH_ITEM.getZeroBased());
        quantityToDeduct = new Quantity("1");
        deleteCommand = new DeleteCommand(INDEX_SIXTH_ITEM, quantityToDeduct);
        expectedModel = new XpireModelManager(xpireModel.getListView(), new UserPrefs());
        expectedXpireItem = new ItemBuilder().withName(VALID_NAME_EXPIRED_MILK)
                .withExpiryDate(VALID_EXPIRY_DATE_EXPIRED_MILK)
                .withQuantity("1")
                .build();
        expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_QUANTITY_SUCCESS,
                quantityToDeduct.toString(), targetXpireItem);
        expectedModel.setItem(targetXpireItem, expectedXpireItem);
        assertCommandSuccess(deleteCommand, xpireModel, expectedMessage, expectedModel);
    }


    @Test
    public void execute_deleteQuantityEqualsToItemQuantityFromItem_success() {
        Quantity quantityToDeduct = new Quantity("1");
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_THIRD_ITEM, quantityToDeduct);
        XpireItem xpireItemToDelete = xpireModel.getFilteredItemList().get(INDEX_THIRD_ITEM.getZeroBased());
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_QUANTITY_SUCCESS,
                quantityToDeduct.toString(), xpireItemToDelete);
        XpireModel expectedXpireModel = new XpireModelManager(xpireModel.getListView(), new UserPrefs());
        expectedXpireModel.deleteItem(xpireItemToDelete);
        assertCommandSuccess(deleteCommand, xpireModel, expectedMessage, expectedXpireModel);
    }

    @Test
    public void execute_deleteQuantityMoreThanItemQuantityFromItem_throwsCommandException() {
        XpireItem xpireItemToDelete = xpireModel.getFilteredItemList().get(INDEX_THIRD_ITEM.getZeroBased());
        Quantity quantityToDeduct = new Quantity("3");
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_THIRD_ITEM, quantityToDeduct);
        assertCommandFailure(deleteCommand, xpireModel, DeleteCommand.MESSAGE_DELETE_QUANTITY_FAILURE);
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

        // different item -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Updates {@code xpireModel}'s filtered list to show no one.
     */
    private void showNoItem(XpireModel xpireModel) {
        xpireModel.updateFilteredItemList(p -> false);

        assertTrue(xpireModel.getFilteredItemList().isEmpty());
    }
}
