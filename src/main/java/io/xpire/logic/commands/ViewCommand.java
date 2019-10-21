package io.xpire.logic.commands;
import static io.xpire.logic.commands.CommandType.XPIRE;
import static java.util.Objects.requireNonNull;

import io.xpire.model.Model;

/**
 * Display all items to the user.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_SUCCESS = "Displayed all items";

    public final CommandType commandType = XPIRE;


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredItemList(Model.PREDICATE_SHOW_ALL_ITEMS);
        return new CommandResult(MESSAGE_SUCCESS);
    }

//    @Override
//    public CommandType getCommandType() {
//        return commandType;
//    }
}
