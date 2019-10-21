package io.xpire.model.state;

import java.util.Stack;

/**
 * The main stack/history manager of the App that uses two stacks.
 * TODO: 2nd stack for redo commands
 */
public class StackManager implements StateStack<State> {

    private final Stack<State> stack;

    public StackManager() {
        this.stack = new Stack<>();
    }

    @Override
    public void push(State state) {
        this.stack.push(state);
    }

    @Override
    public State pop() {
        return this.stack.pop();
    }

    @Override
    public State peek() {
        return this.stack.peek();
    }

    @Override
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }
}
