package io.xpire.logic.commands;

import io.xpire.commons.util.StringUtil;
import io.xpire.model.Model;
import io.xpire.model.item.Item;
import io.xpire.model.state.StateManager;
import javafx.collections.ObservableList;

//@@author JermyTan
/**
 * Exports the list through a QR code.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";
    public static final String COMMAND_SHORTHAND = "ex";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports the current list through a QR code.\n"
            + "Example: " + COMMAND_WORD;

    /** Message to be shown to the user. */
    public static final String SHOWING_EXPORT_MESSAGE = "QR code generated.";

    /** Pretty formatting of the exported data. */
    private static final String BORDER = "* * * * * * * * * * * * * * * * * * * * * * * * *\n";

    @Override
    public CommandResult execute(Model model, StateManager stateManager) {
        ObservableList<? extends Item> currentList = model.getCurrentList();
        StringBuilder formattedOutput = new StringBuilder(BORDER);
        for (int index = 1; index <= currentList.size(); index++) {
            formattedOutput.append(String.format("%d. %s\n", index, currentList.get(index - 1).toString()));
            formattedOutput.append(BORDER);
        }
        byte[] pngData = StringUtil.getQrCode(formattedOutput.toString(), 800);
        return new CommandResult(SHOWING_EXPORT_MESSAGE, true, pngData);
    }
}
