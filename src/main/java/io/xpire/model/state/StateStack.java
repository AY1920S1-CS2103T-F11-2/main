package io.xpire.model.state;

import java.util.Stack;

/**
 * Stack that contains the states of the current working period
 *
 * @param <State> State object.
 */
public class StateStack<State> {

    private final Stack<State> stack;

    public StateStack() {
        this.stack = new Stack<>();
    }

    public void add(State state) {
        this.stack.push(state);
    }

    public State pop() {
        return this.stack.pop();
    }
}
