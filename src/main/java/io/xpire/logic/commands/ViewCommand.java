package io.xpire.logic.commands;
import static java.util.Objects.requireNonNull;

import io.xpire.model.Model;
import io.xpire.model.state.StackManager;
import io.xpire.model.state.State;

/**
 * Display all items to the user.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_SUCCESS = "Displayed all items";


    @Override
    public CommandResult execute(Model model, StackManager stackManager) {
        requireNonNull(model);
        stackManager.push(new State(model, this));
        model.updateFilteredItemList(Model.PREDICATE_SHOW_ALL_ITEMS);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public String toString() {
        return "Test";
    }

}
