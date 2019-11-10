package io.xpire.logic.commands;

import io.xpire.commons.core.Messages;
import io.xpire.commons.core.index.Index;
import io.xpire.model.Model;
import io.xpire.model.ModelManager;
import io.xpire.model.UserPrefs;
import io.xpire.model.item.ExpiryDate;
import io.xpire.model.item.Item;
import io.xpire.model.item.Quantity;
import io.xpire.model.item.XpireItem;
import io.xpire.testutil.XpireItemBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.xpire.logic.commands.CommandTestUtil.assertCommandFailure;
import static io.xpire.logic.commands.CommandTestUtil.assertCommandSuccess;
import static io.xpire.logic.commands.CommandTestUtil.showReplenishItemAtIndex;
import static io.xpire.logic.commands.CommandTestUtil.showXpireItemAtIndex;
import static io.xpire.logic.commands.ShiftToMainCommand.MESSAGE_SUCCESS_SHIFT;
import static io.xpire.logic.commands.ShiftToMainCommand.MESSAGE_SUCCESS_UPDATE_QUANTITY;
import static io.xpire.logic.commands.util.CommandUtil.MESSAGE_REPLENISH_SHIFT_SUCCESS;
import static io.xpire.model.ListType.REPLENISH;
import static io.xpire.model.ListType.XPIRE;
import static io.xpire.testutil.TypicalIndexes.INDEX_EIGHTH_ITEM;
import static io.xpire.testutil.TypicalIndexes.INDEX_FIFTH_ITEM;
import static io.xpire.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static io.xpire.testutil.TypicalIndexes.INDEX_SECOND_ITEM;
import static io.xpire.testutil.TypicalItems.IN_TWO_WEEKS;
import static io.xpire.testutil.TypicalItems.getTypicalLists;
import static io.xpire.testutil.TypicalItemsFields.VALID_EXPIRY_DATE_PAPAYA;
import static io.xpire.testutil.TypicalItemsFields.VALID_NAME_PAPAYA;
import static io.xpire.testutil.TypicalItemsFields.VALID_QUANTITY_APPLE;
import static io.xpire.testutil.TypicalItemsFields.VALID_QUANTITY_PAPAYA;
import static io.xpire.testutil.TypicalItemsFields.VALID_TAG_FRUIT;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Contains integration tests (interaction with the Model, CommandUtil, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteCommand}.
 */
public class ShiftToMainCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalLists(), new UserPrefs());
        model.setCurrentList(REPLENISH);
    }

    @Test
    public void execute_validIndexUnfilteredReplenishList_noDuplicateItem_success() {
        Item itemToShift = model.getCurrentList().get(INDEX_FIRST_ITEM.getZeroBased());
        ShiftToMainCommand shiftToMainCommand = new ShiftToMainCommand(INDEX_FIRST_ITEM,
                new ExpiryDate(IN_TWO_WEEKS), new Quantity(VALID_QUANTITY_APPLE));
        String expectedMessage = String.format(MESSAGE_SUCCESS_SHIFT, itemToShift.getName());
        ModelManager expectedModel = new ModelManager(model.getLists(), new UserPrefs());
        expectedModel.setCurrentList(REPLENISH);
        expectedModel.addItem(XPIRE, itemToShift.remodel(new ExpiryDate(IN_TWO_WEEKS),
                new Quantity(VALID_QUANTITY_APPLE)));
        expectedModel.deleteItem(REPLENISH, itemToShift);
        assertCommandSuccess(shiftToMainCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexUnfilteredReplenishList_duplicateItem_success() {
        Item itemToShift = model.getCurrentList().get(INDEX_FIFTH_ITEM.getZeroBased());
        ShiftToMainCommand shiftToMainCommand = new ShiftToMainCommand(INDEX_FIFTH_ITEM,
                new ExpiryDate(VALID_EXPIRY_DATE_PAPAYA), new Quantity(VALID_QUANTITY_PAPAYA));
        ModelManager expectedModel = new ModelManager(model.getLists(), new UserPrefs());
        expectedModel.setCurrentList(REPLENISH);
        XpireItem expectedUpdatedItem = new XpireItemBuilder().withName(VALID_NAME_PAPAYA)
                                            .withExpiryDate(VALID_EXPIRY_DATE_PAPAYA)
                                            .withQuantity("2")
                                            .withTags(VALID_TAG_FRUIT).build();
        XpireItem itemToUpdate = (XpireItem) expectedModel.getItemList(XPIRE).get(INDEX_EIGHTH_ITEM.getZeroBased());
        expectedModel.setItem(XPIRE, itemToUpdate, expectedUpdatedItem);
        expectedModel.deleteItem(REPLENISH, itemToShift);
        String expectedMessage = String.format(MESSAGE_SUCCESS_UPDATE_QUANTITY, itemToShift.getName(),
                expectedUpdatedItem.getQuantity());
        assertCommandSuccess(shiftToMainCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredXpireList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getCurrentList().size() + 1);
        ShiftToMainCommand shiftToMainCommand = new ShiftToMainCommand(outOfBoundIndex,
                new ExpiryDate(IN_TWO_WEEKS), new Quantity(VALID_QUANTITY_APPLE));
        assertCommandFailure(shiftToMainCommand, model, Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredXpireList_noDuplicateItem_success() {
        model.setCurrentList(REPLENISH);
        showReplenishItemAtIndex(model, INDEX_FIRST_ITEM);

        Item itemToShift = model.getCurrentList().get(INDEX_FIRST_ITEM.getZeroBased());
        ShiftToMainCommand shiftToMainCommand = new ShiftToMainCommand(INDEX_FIRST_ITEM,
                new ExpiryDate(IN_TWO_WEEKS), new Quantity(VALID_QUANTITY_APPLE));

        String expectedMessage = String.format(MESSAGE_SUCCESS_SHIFT, itemToShift.getName());
        Model expectedModel = new ModelManager(model.getLists(), new UserPrefs());
        expectedModel.setCurrentList(REPLENISH);
        expectedModel.addItem(XPIRE, itemToShift.remodel(new ExpiryDate(IN_TWO_WEEKS),
                new Quantity(VALID_QUANTITY_APPLE)));
        expectedModel.deleteItem(REPLENISH, itemToShift);
        assertCommandSuccess(shiftToMainCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexFilteredXpireList_duplicateItem_success() {
        model.setCurrentList(REPLENISH);
        showReplenishItemAtIndex(model, INDEX_FIFTH_ITEM);

        Item itemToShift = model.getCurrentList().get(INDEX_FIRST_ITEM.getZeroBased());

        ShiftToMainCommand shiftToMainCommand = new ShiftToMainCommand(INDEX_FIRST_ITEM,
                new ExpiryDate(VALID_EXPIRY_DATE_PAPAYA), new Quantity(VALID_QUANTITY_PAPAYA));
        ModelManager expectedModel = new ModelManager(model.getLists(), new UserPrefs());
        expectedModel.setCurrentList(REPLENISH);
        XpireItem expectedUpdatedItem = new XpireItemBuilder().withName(VALID_NAME_PAPAYA)
                .withExpiryDate(VALID_EXPIRY_DATE_PAPAYA)
                .withQuantity("2")
                .withTags(VALID_TAG_FRUIT).build();
        XpireItem itemToUpdate = (XpireItem) expectedModel.getItemList(XPIRE).get(INDEX_EIGHTH_ITEM.getZeroBased());
        expectedModel.setItem(XPIRE, itemToUpdate, expectedUpdatedItem);
        expectedModel.deleteItem(REPLENISH, itemToShift);
        showNoItem(expectedModel);
        String expectedMessage = String.format(MESSAGE_SUCCESS_UPDATE_QUANTITY, itemToShift.getName(),
                expectedUpdatedItem.getQuantity());
        assertCommandSuccess(shiftToMainCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredReplenishList_throwsCommandException() {
        model.setCurrentList(REPLENISH);
        showReplenishItemAtIndex(model, INDEX_FIRST_ITEM);
        Index outOfBoundIndex = INDEX_SECOND_ITEM;
        // ensures that outOfBoundIndex is still in bounds of replenish list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getLists()[1].getItemList().size());
        ShiftToMainCommand shiftToMainCommand = new ShiftToMainCommand(outOfBoundIndex,
                new ExpiryDate(IN_TWO_WEEKS), new Quantity(VALID_QUANTITY_APPLE));
        assertCommandFailure(shiftToMainCommand, model, Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
    }

    /**
     * Updates {@code model}'s filtered list to show no item.
     **/
    private void showNoItem(Model model) {
        model.filterCurrentList(REPLENISH, p -> false);
        assertTrue(model.getCurrentList().isEmpty());
    }

}
