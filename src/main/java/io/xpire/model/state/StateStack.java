package io.xpire.model.state;

import java.util.Stack;

/**
 * Stack that contains the states of the current working period
 *
 * @param <State> State object.
 */
public class StateStack<State> {

    public StateStack() {
        Stack<State> stack = new Stack<>();
    }
}
