package io.xpire.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import io.xpire.commons.core.LogsCenter;
import io.xpire.commons.exceptions.DataConversionException;
import io.xpire.model.ReadOnlyListView;
import io.xpire.model.ReadOnlyUserPrefs;
import io.xpire.model.UserPrefs;
import io.xpire.model.item.Item;
import io.xpire.model.item.XpireItem;

/**
 * Manages storage of Xpire data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private XpireStorage xpireStorage;
    private ReplenishStorage replenishStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(XpireStorage xpireStorage, ReplenishStorage replenishStorage,
                          UserPrefsStorage userPrefsStorage) {
        super();
        this.xpireStorage = xpireStorage;
        this.replenishStorage = replenishStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return this.userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return this.userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        this.userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ Xpire methods ==============================

    @Override
    public Path getXpireFilePath() {
        return this.xpireStorage.getXpireFilePath();
    }

    @Override
    public Optional<ReadOnlyListView<XpireItem>> readXpire() throws DataConversionException, IOException {
        return readXpire(this.xpireStorage.getXpireFilePath());
    }

    @Override
    public Optional<ReadOnlyListView<XpireItem>> readXpire(Path filePath) throws
            DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return this.xpireStorage.readXpire(filePath);
    }

    @Override
    public void saveXpire(ReadOnlyListView<XpireItem> xpire) throws IOException {
        saveXpire(xpire, this.xpireStorage.getXpireFilePath());
    }

    @Override
    public void saveXpire(ReadOnlyListView<XpireItem> xpire, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        this.xpireStorage.saveXpire(xpire, filePath);
    }

    // ================ ReplenishList methods ==============================

    @Override
    public Path getReplenishFilePath() {
        return this.replenishStorage.getReplenishFilePath();
    }

    @Override
    public Optional<ReadOnlyListView<Item>> readReplenishList() throws DataConversionException, IOException {
        return readReplenishList(this.replenishStorage.getReplenishFilePath());
    }

    @Override
    public Optional<ReadOnlyListView<Item>> readReplenishList(Path filePath) throws
                                                                          DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return this.replenishStorage.readReplenishList(filePath);
    }

    @Override
    public void saveReplenishList(ReadOnlyListView<Item> replenishList) throws IOException {
        saveReplenishList(replenishList, this.replenishStorage.getReplenishFilePath());
    }

    @Override
    public void saveReplenishList(ReadOnlyListView<Item> replenishList, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        this.replenishStorage.saveReplenishList(replenishList, filePath);
    }
}
