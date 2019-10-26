package io.xpire.logic.commands;

import io.xpire.model.XpireModel;
import org.junit.jupiter.api.Test;

import io.xpire.model.XpireModelManager;
import io.xpire.model.UserPrefs;
import io.xpire.model.ListView;
import io.xpire.testutil.TypicalItems;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBook_success() {
        XpireModel xpireModel = new XpireModelManager();
        XpireModel expectedXpireModel = new XpireModelManager();

        CommandTestUtil.assertCommandSuccess(new ClearCommand(), xpireModel, ClearCommand.MESSAGE_SUCCESS, expectedXpireModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        XpireModel xpireModel = new XpireModelManager(TypicalItems.getTypicalExpiryDateTracker(), new UserPrefs());
        XpireModel expectedXpireModel = new XpireModelManager(TypicalItems.getTypicalExpiryDateTracker(), new UserPrefs());
        expectedXpireModel.setListView(new ListView());

        CommandTestUtil.assertCommandSuccess(new ClearCommand(), xpireModel, ClearCommand.MESSAGE_SUCCESS, expectedXpireModel);
    }

}
