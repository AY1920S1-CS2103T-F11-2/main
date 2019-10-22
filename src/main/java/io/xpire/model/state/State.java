package io.xpire.model.state;

import io.xpire.logic.commands.Command;
import io.xpire.model.CloneModel;
import io.xpire.model.Model;

/**
 * State that stores the current working model.
 */
public class State {
    private final Model model;
    private final Command command;
    private final CloneModel cloneModel;

    public State(Model model, Command command) {
        this.model = model;
        this.cloneModel = new CloneModel(model);
        this.command = command;
    }

    public Model getModel() {
        return this.model;
    }

    public Command getCommand() {
        return this.command;
    }

    public CloneModel getCloneModel() {
        return this.cloneModel;
    }
}
