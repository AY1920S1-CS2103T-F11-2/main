package io.xpire.logic.commands;

import static java.util.Objects.requireNonNull;

import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.Model;
import io.xpire.model.StackManager;
import io.xpire.model.item.ExpiryDate;
import io.xpire.model.item.Name;
import io.xpire.model.item.Quantity;
import io.xpire.model.item.XpireItem;
import io.xpire.model.state.State;

import javax.lang.model.element.QualifiedNameable;
import java.util.List;

/**
 * Adds an xpireItem to the list.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";
    public static final String COMMAND_SHORTHAND = "a";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an item to the tracking list.\n"
            + "Format: add|<item name>|<expiry date>[|<quantity>]\n"
            + "Example: " + COMMAND_WORD + "|Strawberry|11/12/1999|2";

    public static final String MESSAGE_SUCCESS_ITEM_ADDED = "New item added to tracking list: %s";
    public static final String MESSAGE_SUCCESS_ITEM_UPDATED = "Quantity for item is increased to %s";
    public static final String MESSAGE_DUPLICATE_ITEM = "This item already exists";

    private XpireItem toAdd;
    private final Name name;
    private final Quantity quantity;
    private final ExpiryDate expiryDate;

    /**
     * Creates an AddCommand to add the specified {@code XpireItem}
     */
    public AddCommand(Name name, ExpiryDate expiryDate, Quantity quantity) {
        this.name = name;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.toAdd = new XpireItem(this.name, this.expiryDate, this.quantity);
    }

    /**
     * Executes {@code AddCommand}.
     *
     * @param model {@code Model} which the command should operate on.
     * @return success message from {@code CommandResult} if xpireItem is successfully added.
     * @throws CommandException if xpireItem added is a duplicate.
     */
    @Override
    public CommandResult execute(Model model, StackManager stackManager) throws ParseException {
        requireNonNull(model);
        stackManager.saveState(new State(model));
        if (model.hasItem(toAdd)) {
            XpireItem actualItem = model.getXpireItem(toAdd);
            XpireItem newQuantityXpireItem = increaseItemQuantity(new XpireItem(actualItem), this.quantity);
            model.setItem(actualItem, newQuantityXpireItem);
            return new CommandResult(String.format(MESSAGE_SUCCESS_ITEM_UPDATED,
                    newQuantityXpireItem.getQuantity()));
        } else {
            model.addItem(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS_ITEM_ADDED, toAdd));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof AddCommand)) {
            return false;
        } else {
            AddCommand other = (AddCommand) obj;
            return this.toAdd.equals(toAdd);
        }
    }

    @Override
    public int hashCode() {
        return this.toAdd.hashCode();
    }

    @Override
    public String toString() {
        return "Add Command: " + this.toAdd;
    }

    private XpireItem increaseItemQuantity(XpireItem targetItem, Quantity quantity) throws ParseException {
        Quantity prevQuantity = targetItem.getQuantity();
        Quantity updatedQuantity = prevQuantity.increaseQuantity(quantity);
        XpireItem newItem = new XpireItem(targetItem);
        newItem.setQuantity(updatedQuantity);
        return newItem;
    }
}
