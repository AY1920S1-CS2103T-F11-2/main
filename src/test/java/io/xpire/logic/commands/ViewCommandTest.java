package io.xpire.logic.commands;

import static io.xpire.logic.commands.CommandTestUtil.assertCommandSuccess;
import static io.xpire.logic.commands.CommandTestUtil.showItemAtIndex;
import static io.xpire.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static io.xpire.testutil.TypicalItems.getTypicalExpiryDateTracker;

import io.xpire.model.XpireModel;
import io.xpire.model.XpireModelManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.xpire.model.UserPrefs;

/**
 * Contains integration tests (interaction with the XpireModel) and unit tests for ListCommand.
 */
public class ViewCommandTest {

    private XpireModel xpireModel;
    private XpireModel expectedXpireModel;

    @BeforeEach
    public void setUp() {
        xpireModel = new XpireModelManager(getTypicalExpiryDateTracker(), new UserPrefs());
        expectedXpireModel = new XpireModelManager(xpireModel.getListView(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ViewCommand(), xpireModel, ViewCommand.MESSAGE_SUCCESS, expectedXpireModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showItemAtIndex(xpireModel, INDEX_FIRST_ITEM);
        assertCommandSuccess(new ViewCommand(), xpireModel, ViewCommand.MESSAGE_SUCCESS, expectedXpireModel);
    }
}
