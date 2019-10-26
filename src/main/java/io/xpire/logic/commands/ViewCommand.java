package io.xpire.logic.commands;
import static java.util.Objects.requireNonNull;

import io.xpire.model.Model;
import io.xpire.model.XpireModel;

/**
 * Display all items to the user.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_SUCCESS = "Displayed all items";


    @Override
    public CommandResult execute(Model xpireModel) {
        requireNonNull(xpireModel);
        xpireModel.updateFilteredItemList(XpireModel.PREDICATE_SHOW_ALL_ITEMS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
