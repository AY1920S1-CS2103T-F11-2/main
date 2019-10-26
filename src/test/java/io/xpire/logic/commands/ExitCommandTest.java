package io.xpire.logic.commands;

import static io.xpire.logic.commands.CommandTestUtil.assertCommandSuccess;
import static io.xpire.logic.commands.ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT;

import io.xpire.model.XpireModel;
import io.xpire.model.XpireModelManager;
import org.junit.jupiter.api.Test;

public class ExitCommandTest {
    private XpireModel xpireModel = new XpireModelManager();
    private XpireModel expectedXpireModel = new XpireModelManager();

    @Test
    public void execute_exit_success() {
        CommandResult expectedCommandResult = new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
        assertCommandSuccess(new ExitCommand(), xpireModel, expectedCommandResult, expectedXpireModel);
    }
}
