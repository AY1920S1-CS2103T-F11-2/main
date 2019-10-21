package io.xpire.model.state;

import io.xpire.model.Model;

/**
 * State that stores the current working model.
 */
public class State {
    private final Model model;

    public State(Model prevModel) {
        this.model = prevModel;
    }

    public Model getModel() {
        return this.model;
    }
}
