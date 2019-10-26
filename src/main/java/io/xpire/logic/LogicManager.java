package io.xpire.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import io.xpire.commons.core.GuiSettings;
import io.xpire.commons.core.LogsCenter;
import io.xpire.logic.commands.Command;
import io.xpire.logic.commands.CommandResult;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.logic.parser.XpireParser;
import io.xpire.logic.parser.exceptions.ParseException;
import io.xpire.model.Model;
import io.xpire.model.ReplenishModel;
import io.xpire.model.XpireModel;
import io.xpire.model.ReadOnlyListView;
import io.xpire.model.item.XpireItem;
import io.xpire.storage.Storage;
import javafx.collections.ObservableList;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private Model model;
    private final XpireModel xpireModel;
    private final ReplenishModel replenishModel;
    private final Storage storage;
    private final XpireParser xpireParser;
    private Mode mode;

    public LogicManager(XpireModel xpireModel, ReplenishModel replenishModel, Storage storage) {
        this.xpireModel = xpireModel;
        this.replenishModel = replenishModel;
        this.storage = storage;
        this.xpireParser = new XpireParser();
        this.mode = Mode.XPIRE;
        updateModel();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = this.xpireParser.parseCommand(commandText);
        commandResult = command.execute(this.model);

        try {
            this.storage.updateListStorage(mode);
            this.storage.saveList(this.model.getListView());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyListView getListView() {
        return this.model.getListView();
    }

    @Override
    public ObservableList<XpireItem> getFilteredItemList() {
        return this.model.getFilteredItemList();
    }

    @Override
    public Path getListFilePath() {
        return this.model.getListFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return this.model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        this.model.setGuiSettings(guiSettings);
    }

    private void updateModel() {
        this.model = xpireModel;
    }

}
