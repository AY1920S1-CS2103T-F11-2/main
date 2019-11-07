package io.xpire.model.state;

/**
 * An interface that provides methods for altering States.
 */
public interface StateManager {

    void saveState(State state);

    boolean isNotRedoable();

    boolean isNotUndoable();

    State undo(State state);

    State redo();

}
