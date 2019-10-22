package io.xpire.logic.commands;

import static java.util.Objects.requireNonNull;

import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.model.Model;

/**
 * Display all items to the user.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String XPIRE_MESSAGE_SUCCESS = "Displayed all items";

    public static final String REPLENISH_MESSAGE_SUCCESS = "Displayed all items in the replenish list.\n"
            + "To move an item in the replenish list to the tracker"
            + ReplaceCommand.MESSAGE_USAGE_FORMAT;

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows the list of items.\n"
            + "If the key is not specified, the expiry date tracker list will be displayed.\n"
            + "If the key is specified as 'replenish', the replenish list will be displayed. \n"
            + "Format: view[|<key>] (where key is 'replenish')\n"
            + "Example: " + COMMAND_WORD + "|replenish";

    private enum RequestedView {
        TRACKER, REPLENISHLIST;
    }

    private final RequestedView requestedView;

    public ViewCommand() {
        this.requestedView = RequestedView.TRACKER;
    }

    public ViewCommand(String s) {
        this.requestedView = RequestedView.REPLENISHLIST;
    }

    @Override
    public CommandResult execute(Model model, boolean isReplenishView) throws CommandException {
        return execute(model);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        switch (this.requestedView) {
        case TRACKER:
            model.updateFilteredItemList(Model.PREDICATE_SHOW_ALL_ITEMS);
            return new CommandResult(XPIRE_MESSAGE_SUCCESS);
        case REPLENISHLIST:
            model.updateFilteredReplenishList(Model.PREDICATE_SHOW_ALL_TO_BUY);
            return new CommandResult(REPLENISH_MESSAGE_SUCCESS, false, false, true);
        default:
            assert false;
            return null;
        }
    }

}
