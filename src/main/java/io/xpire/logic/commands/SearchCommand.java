package io.xpire.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import io.xpire.commons.core.Messages;
import io.xpire.logic.parser.XpireParser;
import io.xpire.model.Model;
import io.xpire.model.item.ContainsKeywordsPredicate;
import io.xpire.model.item.Name;
import io.xpire.model.tag.Tag;

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
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredItemList(this.predicate);
        StringBuilder sb = new StringBuilder(String.format(Messages.MESSAGE_ITEMS_LISTED_OVERVIEW,
                model.getFilteredItemList().size()));
        if (model.getFilteredItemList().size() == 0) {
            for (String s: predicate.getKeywords()) {
                if (s.startsWith("#")) {
                    Set<String> tagList = model.getAllItemTags().stream()
                                               .map(Tag::toString)
                                               .collect(Collectors.toSet());
                    sb.append(XpireParser.findSimilar(s, tagList, 3));
                } else {
                    Set<String> nameList = model.getAllItemNames().stream()
                                                .map(Name::toString)
                                                .map(x-> x.split("\\s+"))
                                                .flatMap(Arrays::stream)
                                                .collect(Collectors.toSet());
                    sb.append(XpireParser.findSimilar(s, nameList, 3));
                }
            }
        }
        return new CommandResult(sb.toString());
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
