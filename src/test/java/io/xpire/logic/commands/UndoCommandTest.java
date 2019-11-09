package io.xpire.logic.commands;

import static io.xpire.logic.commands.CommandTestUtil.assertCommandSuccess;
import static io.xpire.logic.commands.CommandTestUtil.executeCommandAndUpdateStateManager;
import static io.xpire.logic.commands.UndoCommand.MESSAGE_UNDO_SUCCESS;
import static io.xpire.model.ListType.REPLENISH;
import static io.xpire.model.ListType.XPIRE;
import static io.xpire.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static io.xpire.testutil.TypicalIndexes.INDEX_SECOND_ITEM;
import static io.xpire.testutil.TypicalIndexes.INDEX_THIRD_ITEM;
import static io.xpire.testutil.TypicalItems.getTypicalLists;
import static io.xpire.testutil.TypicalItemsFields.VALID_EXPIRY_DATE_KIWI;
import static io.xpire.testutil.TypicalItemsFields.VALID_NAME_KIWI;
import static io.xpire.testutil.TypicalItemsFields.VALID_QUANTITY_KIWI;
import static io.xpire.testutil.TypicalItemsFields.VALID_TAG_FRIDGE;
import static io.xpire.testutil.TypicalItemsFields.VALID_TAG_FRUIT;
import static io.xpire.testutil.TypicalItemsFields.VALID_TAG_PROTEIN;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.Model;
import io.xpire.model.ModelManager;
import io.xpire.model.UserPrefs;
import io.xpire.model.item.ContainsKeywordsPredicate;
import io.xpire.model.item.ExpiringSoonPredicate;
import io.xpire.model.item.ExpiryDate;
import io.xpire.model.item.Name;
import io.xpire.model.item.Quantity;
import io.xpire.model.item.ReminderThreshold;
import io.xpire.model.item.ReminderThresholdExceededPredicate;
import io.xpire.model.item.sort.XpireMethodOfSorting;
import io.xpire.model.state.StackManager;
import io.xpire.model.state.StateManager;
import io.xpire.model.tag.Tag;
import io.xpire.model.tag.TagComparator;

public class UndoCommandTest {

    private Model model;
    private StateManager stateManager;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalLists(), new UserPrefs());
        stateManager = new StackManager();
    }

    //--------------------XPIRE VIEW-----------------------------------------------------------------------

    //Testing Undo for AddCommand
    @Test
    public void execute_undoAddCommand_success() throws CommandException, ParseException {
        AddCommand addCommand = new AddCommand(new Name(VALID_NAME_KIWI), new ExpiryDate(VALID_EXPIRY_DATE_KIWI),
                new Quantity(VALID_QUANTITY_KIWI));
        executeCommandAndUpdateStateManager(model, addCommand, stateManager);
        Model expectedModel = new ModelManager(getTypicalLists(), new UserPrefs());
        UndoCommand undoCommand = new UndoCommand();
        CommandResult expectedMessage = new CommandResult(MESSAGE_UNDO_SUCCESS);
        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel, stateManager);
    }

    //Testing Undo for CheckCommand(ReminderThresholdExceededPredicate)
    @Test
    public void execute_undoCheckCommandReminderPredicate_success() throws CommandException, ParseException {
        CheckCommand checkCommand = new CheckCommand(new ReminderThresholdExceededPredicate());
        executeCommandAndUpdateStateManager(model, checkCommand, stateManager);
        Model expectedModel = new ModelManager(getTypicalLists(), new UserPrefs());
        UndoCommand undoCommand = new UndoCommand();
        CommandResult expectedMessage = new CommandResult(MESSAGE_UNDO_SUCCESS);
        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel, stateManager);
    }

    //Testing Undo for CheckCommand(ExpiringSoonPredicate)
    @Test
    public void execute_undoCheckCommandExpiringPredicate_success() throws CommandException, ParseException {
        CheckCommand checkCommand = new CheckCommand(new ExpiringSoonPredicate(12), 12);
        executeCommandAndUpdateStateManager(model, checkCommand, stateManager);
        Model expectedModel = new ModelManager(getTypicalLists(), new UserPrefs());
        UndoCommand undoCommand = new UndoCommand();
        CommandResult expectedMessage = new CommandResult(MESSAGE_UNDO_SUCCESS);
        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel, stateManager);
    }

    //Testing Undo for ClearCommand
    @Test
    public void execute_undoClear_success() throws CommandException, ParseException {
        ClearCommand clearCommand = new ClearCommand(XPIRE);
        executeCommandAndUpdateStateManager(model, clearCommand, stateManager);
        Model expectedModel = new ModelManager(getTypicalLists(), new UserPrefs());
        UndoCommand undoCommand = new UndoCommand();
        CommandResult expectedMessage = new CommandResult(MESSAGE_UNDO_SUCCESS);
        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel, stateManager);
    }

    //Testing Undo for DeleteCommand(Item)
    @Test
    public void execute_undoDeleteItem_success() throws CommandException, ParseException {
        DeleteCommand deleteCommand = new DeleteCommand(XPIRE, INDEX_FIRST_ITEM);
        executeCommandAndUpdateStateManager(model, deleteCommand, stateManager);
        Model expectedModel = new ModelManager(getTypicalLists(), new UserPrefs());
        UndoCommand undoCommand = new UndoCommand();
        CommandResult expectedMessage = new CommandResult(MESSAGE_UNDO_SUCCESS);
        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel, stateManager);
    }

    //Testing Undo for DeleteCommand(Tags)
    @Test
    public void execute_undoDeleteTags_success() throws CommandException, ParseException {
        Set<Tag> set = new TreeSet<>(new TagComparator());
        set.add(new Tag(VALID_TAG_FRIDGE));
        set.add(new Tag(VALID_TAG_PROTEIN));
        DeleteCommand deleteCommand = new DeleteCommand(XPIRE, INDEX_THIRD_ITEM, set);
        executeCommandAndUpdateStateManager(model, deleteCommand, stateManager);
        Model expectedModel = new ModelManager(getTypicalLists(), new UserPrefs());
        UndoCommand undoCommand = new UndoCommand();
        CommandResult expectedMessage = new CommandResult(MESSAGE_UNDO_SUCCESS);
        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel, stateManager);
    }

    //Testing Undo for DeleteCommand(Quantity)
    @Test
    public void execute_undoDeleteQuantity_success() throws CommandException, ParseException {
        Quantity quantityToDeduct = new Quantity("2");
        DeleteCommand deleteCommand = new DeleteCommand(XPIRE, INDEX_SECOND_ITEM, quantityToDeduct);
        executeCommandAndUpdateStateManager(model, deleteCommand, stateManager);
        Model expectedModel = new ModelManager(getTypicalLists(), new UserPrefs());
        UndoCommand undoCommand = new UndoCommand();
        CommandResult expectedMessage = new CommandResult(MESSAGE_UNDO_SUCCESS);
        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel, stateManager);
    }

    //Testing Undo for DeleteCommand(Quantity + Shift)
    @Test
    public void execute_undoDeleteQuantityAndShift_success() throws CommandException, ParseException {
        Quantity quantityToDeduct = new Quantity("1");
        DeleteCommand deleteCommand = new DeleteCommand(XPIRE, INDEX_THIRD_ITEM, quantityToDeduct);
        executeCommandAndUpdateStateManager(model, deleteCommand, stateManager);
        Model expectedModel = new ModelManager(getTypicalLists(), new UserPrefs());
        UndoCommand undoCommand = new UndoCommand();
        CommandResult expectedMessage = new CommandResult(MESSAGE_UNDO_SUCCESS);
        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel, stateManager);
    }

    //Testing Undo for SearchCommand
    @Test
    public void execute_undoSearch_success() throws CommandException, ParseException {
        ContainsKeywordsPredicate predicate = new ContainsKeywordsPredicate(Arrays.asList("Pineapple|Pear|#Cold"
                .split("\\|")));
        SearchCommand searchCommand = new SearchCommand(XPIRE, predicate);
        executeCommandAndUpdateStateManager(model, searchCommand, stateManager);
        Model expectedModel = new ModelManager(getTypicalLists(), new UserPrefs());
        UndoCommand undoCommand = new UndoCommand();
        CommandResult expectedMessage = new CommandResult(MESSAGE_UNDO_SUCCESS);
        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel, stateManager);
    }

    //Testing Undo for SetReminderCommand
    @Test
    public void execute_undoSetReminder_success() throws CommandException, ParseException {
        ReminderThreshold threshold = new ReminderThreshold("1");
        SetReminderCommand setReminderCommand = new SetReminderCommand(INDEX_SECOND_ITEM, threshold);
        executeCommandAndUpdateStateManager(model, setReminderCommand, stateManager);
        Model expectedModel = new ModelManager(getTypicalLists(), new UserPrefs());
        UndoCommand undoCommand = new UndoCommand();
        CommandResult expectedMessage = new CommandResult(MESSAGE_UNDO_SUCCESS);
        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel, stateManager);
    }

    //Testing Undo for ShiftToReplenishCommand
    @Test
    public void execute_undoShiftToReplenish_success() throws CommandException, ParseException {
        ShiftToReplenishCommand shiftToReplenishCommand = new ShiftToReplenishCommand(INDEX_SECOND_ITEM);
        executeCommandAndUpdateStateManager(model, shiftToReplenishCommand, stateManager);
        Model expectedModel = new ModelManager(getTypicalLists(), new UserPrefs());
        UndoCommand undoCommand = new UndoCommand();
        CommandResult expectedMessage = new CommandResult(MESSAGE_UNDO_SUCCESS);
        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel, stateManager);
    }

    //Testing Undo for SortCommand
    @Test
    public void execute_undoSort_success() throws CommandException, ParseException {
        XpireMethodOfSorting xpireMethodOfSorting = new XpireMethodOfSorting("date");
        SortCommand sortCommand = new SortCommand(xpireMethodOfSorting);
        executeCommandAndUpdateStateManager(model, sortCommand, stateManager);
        Model expectedModel = new ModelManager(getTypicalLists(), new UserPrefs());
        UndoCommand undoCommand = new UndoCommand();
        CommandResult expectedMessage = new CommandResult(MESSAGE_UNDO_SUCCESS);
        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel, stateManager);
    }

    //Testing Undo for TagCommand
    @Test
    public void execute_undoTag_success() throws CommandException, ParseException {
        TagCommand tagCommand = new TagCommand(XPIRE, INDEX_FIRST_ITEM,
                new String[]{VALID_TAG_FRIDGE, VALID_TAG_FRUIT});
        executeCommandAndUpdateStateManager(model, tagCommand, stateManager);
        Model expectedModel = new ModelManager(getTypicalLists(), new UserPrefs());
        UndoCommand undoCommand = new UndoCommand();
        CommandResult expectedMessage = new CommandResult(MESSAGE_UNDO_SUCCESS);
        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel, stateManager);
    }

    //Testing Undo for ViewCommand
    @Test
    public void execute_undoView_success() throws CommandException, ParseException {
        ViewCommand viewCommand = new ViewCommand(REPLENISH);
        executeCommandAndUpdateStateManager(model, viewCommand, stateManager);
        Model expectedModel = new ModelManager(getTypicalLists(), new UserPrefs());
        UndoCommand undoCommand = new UndoCommand();
        CommandResult expectedMessage = new CommandResult(MESSAGE_UNDO_SUCCESS);
        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel, stateManager);
    }

    //--------------------REPLENISH VIEW-----------------------------------------------------------------------
    //Testing Undo for ShiftToMainCommand
    //Testing Undo for Multiple Commands
    //Testing Undo for RedoCommand(Should not Undo)
    //Testing Undo for TagCommand(Show)(Should not Undo)
    //Testing Undo for ExitCommand(Should not Undo)
    //Testing Undo for HelpCommand(Should not Undo)
    //Testing Undo for ExportCommand(Should not Undo)
    //Testing Undo if there are no Undoable States
}
