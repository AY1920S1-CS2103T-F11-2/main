package io.xpire.model.state;

/**
 * Stack Interface that contains the states of the current working period
 *
 * @param <State> State object.
 */
public interface StateStack<State> {

    public void push(State state);

    public State pop();

    public boolean isEmpty();

    public State peek();
}
