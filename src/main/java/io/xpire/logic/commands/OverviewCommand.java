package io.xpire.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.Model;
import io.xpire.model.StackManager;
import io.xpire.model.item.ExpiringSoonPredicate;
import io.xpire.model.item.Item;
import io.xpire.model.item.ReminderThresholdExceededPredicate;
import io.xpire.model.item.XpireItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Shows an overview of all the items in Xpire.
 */
public class OverviewCommand extends Command {

    public static final String COMMAND_WORD = "overview";
    public static final String COMMAND_SHORTHAND = "o";

    private boolean showDetails;

    public OverviewCommand(boolean showDetails) {
        this.showDetails = showDetails;
    }

    @Override
    public CommandResult execute(Model model, StackManager stackManager) throws CommandException, ParseException {
        requireNonNull(model);
        ObservableList<XpireItem> xpireList = model.getFilteredXpireItemList();
        ObservableList<Item> replenishList = model.getFilteredReplenishItemList();

        ObservableList<XpireItem> expiringSoonItems = this.filterXpireList(xpireList,
                new ReminderThresholdExceededPredicate());
        ObservableList<XpireItem> expiringWithinAWeekItems = this.filterXpireList(xpireList,
                new ExpiringSoonPredicate(7));
        ObservableList<XpireItem> expiringWithinTwoWeeksItems = this.filterXpireList(xpireList,
                new ExpiringSoonPredicate(14));

        StringBuilder overview = new StringBuilder();

        overview.append(String.format("Total number of items in Xpire: %d\n\n",
                xpireList.size() + replenishList.size()));

        overview.append(String.format("Total number of items in tracking list: %d\n",
                xpireList.size()));
        overview.append(String.format("Number of items that are expiring soon or have expired: %d\n",
                expiringSoonItems.size()));
        overview.append(String.format("Number of items that are expiring within a week: %d\n",
                expiringWithinAWeekItems.size()));
        overview.append(String.format("Number of items that are expiring within two weeks: %d\n",
                expiringWithinTwoWeeksItems.size()));
        overview.append("Tags created in tracking list and the number of items in each tag:");

        overview.append(String.format("Total number of items in to-buy list: %d\n\n", replenishList.size()));

        return new CommandResult(overview.toString());
    }

    private ObservableList<XpireItem> filterXpireList(ObservableList<XpireItem> items, Predicate<XpireItem> predicate) {
        ObservableList<XpireItem> itemsCopy = FXCollections.observableArrayList(items);
        return itemsCopy.filtered(predicate::test);
    }
}
