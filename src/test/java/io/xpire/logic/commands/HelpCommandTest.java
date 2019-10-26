package io.xpire.logic.commands;

import io.xpire.model.XpireModelManager;
import org.junit.jupiter.api.Test;

import io.xpire.model.XpireModel;

public class HelpCommandTest {
    private XpireModel xpireModel = new XpireModelManager();
    private XpireModel expectedXpireModel = new XpireModelManager();

    @Test
    public void execute_help_success() {
        CommandResult expectedCommandResult =
                new CommandResult(HelpCommand.SHOWING_HELP_MESSAGE, true, false);
        CommandTestUtil.assertCommandSuccess(new HelpCommand(), xpireModel, expectedCommandResult, expectedXpireModel);
    }
}
