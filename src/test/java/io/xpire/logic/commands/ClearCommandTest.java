package io.xpire.logic.commands;

import org.junit.jupiter.api.Test;

import io.xpire.model.Model;
import io.xpire.model.ModelManager;
import io.xpire.model.UserPrefs;
import io.xpire.model.ListView;
import io.xpire.testutil.TypicalItems;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        CommandTestUtil.assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        Model model = new ModelManager(TypicalItems.getTypicalExpiryDateTracker(), new UserPrefs());
        Model expectedModel = new ModelManager(TypicalItems.getTypicalExpiryDateTracker(), new UserPrefs());
        expectedModel.setXpire(new ListView());

        CommandTestUtil.assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
