package io.xpire.logic.commands;

import static io.xpire.commons.util.CollectionUtil.requireAllNonNull;

import io.xpire.model.ListType;
import io.xpire.model.Model;
import io.xpire.model.item.Item;
import io.xpire.model.state.ModifiedState;
import io.xpire.model.state.StateManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Clears all items in the list.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String COMMAND_SHORTHAND = "cl";

    public static final String MESSAGE_SUCCESS = "Current list has been cleared!";

    private final ListType listType;

    public ClearCommand(ListType listType) {
        this.listType = listType;
    }

    @Override
    public CommandResult execute(Model model, StateManager stateManager) {
        requireAllNonNull(model, stateManager);
        stateManager.saveState(new ModifiedState(model));

        //remove list dependency on xpire/replenish internal list
        ObservableList<? extends Item> currentList = FXCollections.observableArrayList(model.getCurrentList());
        currentList.forEach(item -> model.deleteItem(this.listType, item));

        setShowInHistory(true);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public String toString() {
        return "Clear command";
    }
}
