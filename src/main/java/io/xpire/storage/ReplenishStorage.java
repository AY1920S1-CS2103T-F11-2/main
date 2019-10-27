package io.xpire.storage;

import io.xpire.commons.exceptions.DataConversionException;
import io.xpire.model.ReadOnlyListView;
import io.xpire.model.item.Item;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public interface ReplenishStorage {
    /**
     * Returns the file path of the data file.
     */
    Path getReplenishFilePath();

    /**
     * Returns ReplenishList data as a {@link ReadOnlyListView<Item>}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyListView<Item>> readReplenishList() throws DataConversionException, IOException;

    /**
     * @see #getReplenishFilePath()
     */
    Optional<ReadOnlyListView<Item>> readReplenishList(Path filePath) throws DataConversionException,
                                                               IOException;

    /**
     * Saves the given {@link ReadOnlyListView} to the storage.
     * @param replenishList cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveReplenishList(ReadOnlyListView<Item> replenishList) throws IOException;

    /**
     * @see #saveReplenishList(ReadOnlyListView)
     */
    void saveReplenishList(ReadOnlyListView<Item> replenishList, Path filePath) throws IOException;

}
