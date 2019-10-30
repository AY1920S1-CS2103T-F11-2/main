package io.xpire.logic;

import java.nio.file.Path;

import io.xpire.commons.core.GuiSettings;
import io.xpire.logic.commands.CommandResult;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.Model;
import io.xpire.model.ReadOnlyListView;
import io.xpire.model.item.Item;
import javafx.collections.transformation.FilteredList;

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
     * Returns an array containing both a replenish list object and xpire object.
     *
     * @see Model#getLists()
     * @return array of ReadOnlyListView objects.
     */
    ReadOnlyListView<? extends Item>[] getLists();

    /** Returns an unmodifiable view of the current filtered list of items */
    FilteredList<? extends Item> getCurrentFilteredItemList();

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
