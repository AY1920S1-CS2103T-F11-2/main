package io.xpire.logic.commands;

import static java.util.Objects.requireNonNull;

import io.xpire.model.Model;

/**
 * Display all items to the user.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_SUCCESS = "Displayed all items";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows the list of items.\n"
            + "If the key is not specified, the expiry date tracker list will be displayed.\n"
            + "If the key is specified as 'replenish', the replenish list will be displayed. \n"
            + "Format: view[|<key>] (where key is 'replenish')\n"
            + "Example: " + COMMAND_WORD + "|replenish";


    private enum ViewMode { REPLENISH, XPIRE }

    private final ViewMode viewMode;

    public ViewCommand() {
        this.viewMode = ViewMode.XPIRE;
    }

    public ViewCommand(String s) {
        this.viewMode = ViewMode.REPLENISH;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        switch (this.viewMode) {
        case XPIRE:
            model.updateFilteredItemList(Model.PREDICATE_SHOW_ALL_ITEMS);
            return new CommandResult(MESSAGE_SUCCESS);
        case REPLENISH:
            model.updateFilteredReplenishList(Model.PREDICATE_SHOW_ALL_TO_BUY);
            return new CommandResult(MESSAGE_SUCCESS, false, false, true);
        default:
            assert false;
            return null;
        }
    }

}
