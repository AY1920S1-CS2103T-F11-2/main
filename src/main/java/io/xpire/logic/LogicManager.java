package io.xpire.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;
import io.xpire.commons.core.GuiSettings;
import io.xpire.commons.core.LogsCenter;
import io.xpire.logic.commands.Command;
import io.xpire.logic.commands.CommandResult;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.logic.parser.Parser;
import io.xpire.logic.parser.ReplenishParser;
import io.xpire.logic.parser.XpireParser;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.Model;
import io.xpire.model.ReadOnlyListView;
import io.xpire.model.item.Item;
import io.xpire.storage.Storage;
import javafx.collections.ObservableList;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private Parser xpireParser;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        this.xpireParser = new XpireParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        if (this.model.getCurrentFilteredItemList() == this.model.getFilteredXpireItemList()) {
            this.xpireParser = new XpireParser();
        } else {
            this.xpireParser = new ReplenishParser();
        }
        Command command = this.xpireParser.parse(commandText);
        commandResult = command.execute(this.model);

        try {
            this.storage.saveXpire(this.model.getXpire());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyListView<? extends Item>[] getXpire() {
        return this.model.getXpire();
    }

    @Override
    public ObservableList<? extends Item> getCurrentFilteredItemList() {
        return this.model.getCurrentFilteredItemList();
    }

    @Override
    public Path getXpireFilePath() {
        return this.model.getXpireFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return this.model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        this.model.setGuiSettings(guiSettings);
    }
}
