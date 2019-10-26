package io.xpire.logic.commands;

import static io.xpire.logic.commands.CommandTestUtil.assertCommandFailure;
import static io.xpire.logic.commands.CommandTestUtil.assertCommandSuccess;

import io.xpire.model.XpireModel;
import io.xpire.model.XpireModelManager;
import io.xpire.model.item.XpireItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.xpire.model.UserPrefs;
import io.xpire.testutil.ItemBuilder;
import io.xpire.testutil.TypicalItems;

/**
 * Contains integration tests (interaction with the XpireModel) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private XpireModel xpireModel;

    @BeforeEach
    public void setUp() {
        xpireModel = new XpireModelManager(TypicalItems.getTypicalExpiryDateTracker(), new UserPrefs());
    }

    @Test
    public void execute_newItem_success() {
        XpireItem validXpireItem = new ItemBuilder().build();

        XpireModel expectedXpireModel = new XpireModelManager(xpireModel.getListView(), new UserPrefs());
        expectedXpireModel.addItem(validXpireItem);

        assertCommandSuccess(new AddCommand(validXpireItem), xpireModel,
                String.format(AddCommand.MESSAGE_SUCCESS, validXpireItem), expectedXpireModel);
    }

    @Test
    public void execute_duplicateItem_throwsCommandException() {
        XpireItem xpireItemInList = xpireModel.getListView().getItemList().get(0);
        assertCommandFailure(new AddCommand(xpireItemInList), xpireModel, AddCommand.MESSAGE_DUPLICATE_ITEM);
    }

}
