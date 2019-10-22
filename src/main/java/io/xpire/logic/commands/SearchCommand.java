package io.xpire.logic.commands;

import static io.xpire.commons.core.Messages.MESSAGE_VIEW_MODE;
import static java.util.Objects.requireNonNull;

import io.xpire.commons.core.Messages;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.model.Model;
import io.xpire.model.item.ContainsKeywordsPredicate;

/**
 * Searches and displays all items whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class SearchCommand extends Command {

    public static final String COMMAND_WORD = "search";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Searches and displays all items whose names "
            + "or tag(s) contain any of the specified keywords (case-insensitive).\n"
            + "Searching for names allows partial match while searching for tags requires exact match.\n"
            + "Format: search|<keyword>[|<other keywords>]... (keyword(s) for tags must be prefixed with a '#')\n"
            + "Example: " + COMMAND_WORD + "|apple|#fridge|banana";

    private final ContainsKeywordsPredicate predicate;

    public SearchCommand(ContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, boolean isReplenishView) throws CommandException {
        if (isReplenishView) {
            throw new CommandException(MESSAGE_VIEW_MODE);
        } else {
            return execute(model);
        }
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredItemList(this.predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_ITEMS_LISTED_OVERVIEW, model.getFilteredItemList().size()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof SearchCommand)) {
            return false;
        } else {
            SearchCommand other = (SearchCommand) obj;
            return this.predicate.equals(other.predicate);
        }
    }

    @Override
    public int hashCode() {
        return this.predicate.hashCode();
    }
}
