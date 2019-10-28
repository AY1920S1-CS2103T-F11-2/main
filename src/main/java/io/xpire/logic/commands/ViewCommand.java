package io.xpire.logic.commands;
import static java.util.Objects.requireNonNull;

import io.xpire.model.Model;

/**
 * Display all items to the user.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_SUCCESS = "Displayed all items";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Views all items in tracking or toreplenish list.\n"
            + "If key is 'replenish', items in the toReplenish list will be displayed.\n"
            + "If key is 'main', items in the main list will be displayed.\n"
            + "Format: view|<key> (where key is 'main' or 'replenish')\n"
            + "Example: " + COMMAND_WORD + "|main";

    public final String list;

    public ViewCommand(String list) {
        this.list = list;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        switch (list) {
        case "main":
            model.updateFilteredItemList(Model.PREDICATE_SHOW_ALL_ITEMS);
            return new CommandResult(MESSAGE_SUCCESS + " in main list.");
        case "replenish":
            model.updateFilteredReplenishItemList(Model.PREDICATE_SHOW_ALL_REPLENISH_ITEMS);
            return new CommandResult(MESSAGE_SUCCESS + " in replenish list");
        default:
            return null;
        }
    }
}
