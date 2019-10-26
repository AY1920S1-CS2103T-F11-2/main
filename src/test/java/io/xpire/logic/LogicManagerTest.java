package io.xpire.logic;

import static io.xpire.commons.core.Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX;
import static io.xpire.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import static io.xpire.logic.CommandParserItemUtil.VALID_EXPIRY_DATE_BANANA;
import static io.xpire.logic.CommandParserItemUtil.VALID_NAME_BANANA;
import static io.xpire.logic.CommandParserItemUtil.VALID_QUANTITY_BANANA;

import static io.xpire.testutil.Assert.assertThrows;
import static io.xpire.testutil.TypicalItems.BANANA;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;

import io.xpire.model.XpireModel;
import io.xpire.model.XpireModelManager;
import io.xpire.model.item.XpireItem;
import io.xpire.storage.JsonXpireStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.xpire.logic.commands.AddCommand;
import io.xpire.logic.commands.CommandResult;
import io.xpire.logic.commands.ViewCommand;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.ReadOnlyListView;
import io.xpire.model.UserPrefs;
import io.xpire.storage.JsonUserPrefsStorage;
import io.xpire.storage.StorageManager;
import io.xpire.testutil.ItemBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy exception");

    @TempDir
    public Path temporaryFolder;

    private XpireModel xpireModel = new XpireModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonXpireStorage addressBookStorage =
                new JsonXpireStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(xpireModel, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete|9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ViewCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ViewCommand.MESSAGE_SUCCESS, xpireModel);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        // Setup LogicManager with JsonAddressBookIoExceptionThrowingStub
        JsonXpireStorage addressBookStorage =
                new JsonXpireIoExceptionThrowingStub(
                        temporaryFolder.resolve("ioExceptionAddressBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ioExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(xpireModel, storage);

        // Execute add command
        String addCommand = AddCommand.COMMAND_WORD + "|" + VALID_NAME_BANANA + "|" + VALID_EXPIRY_DATE_BANANA
                + "| " + VALID_QUANTITY_BANANA;
        XpireItem expectedXpireItem = new ItemBuilder(BANANA).build();
        XpireModelManager expectedModel = new XpireModelManager();
        expectedModel.addItem(expectedXpireItem);
        String expectedMessage = LogicManager.FILE_OPS_ERROR_MESSAGE + DUMMY_IO_EXCEPTION;
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    @Test
    public void getFilteredItemList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredItemList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal xpireModel manager state is the same as that in {@code expectedXpireModel} <br>
     * @see #assertCommandFailure(String, Class, String, XpireModel)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            XpireModel expectedXpireModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedXpireModel, xpireModel);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, XpireModel)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, XpireModel)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, XpireModel)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        XpireModel expectedXpireModel = new XpireModelManager(xpireModel.getListView(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedXpireModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal xpireModel manager state is the same as that in {@code expectedXpireModel} <br>
     * @see #assertCommandSuccess(String, String, XpireModel)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, XpireModel expectedXpireModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedXpireModel, xpireModel);
    }

    /**
     * A stub class to throw an {@code IOException} when the save method is called.
     */
    private static class JsonXpireIoExceptionThrowingStub extends JsonXpireStorage {
        private JsonXpireIoExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveList(ReadOnlyListView xpire, Path filePath) throws IOException {
            throw DUMMY_IO_EXCEPTION;
        }
    }
}
