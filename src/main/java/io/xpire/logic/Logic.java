package io.xpire.logic;

import java.nio.file.Path;

import io.xpire.commons.core.GuiSettings;
import io.xpire.logic.commands.CommandResult;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.XpireModel;
import io.xpire.model.ReadOnlyListView;
import io.xpire.model.item.XpireItem;
import javafx.collections.ObservableList;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     *
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns an xpire object.
     *
     * @see XpireModel#getListView()
     */
    ReadOnlyListView getListView();

    /** Returns an unmodifiable view of the filtered list of items */
    ObservableList<XpireItem> getFilteredItemList();

    /**
     * Returns the user prefs' xpire file path.
     */
    Path getListFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);
}
