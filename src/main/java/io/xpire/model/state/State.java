package io.xpire.model.state;

import io.xpire.logic.commands.Command;
import io.xpire.model.Model;

/**
 * State that stores the current working model.
 */
public class State {
    private final Model model;
    private final Command command;

    public State(Model model, Command command) {
        this.model = model;
        this.command = command;
    }

    public Model getModel() {
        return this.model;
    }

    public Command getCommand() {
        return this.command;
    }
}
