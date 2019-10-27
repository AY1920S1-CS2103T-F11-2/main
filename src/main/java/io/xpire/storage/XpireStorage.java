package io.xpire.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import io.xpire.commons.exceptions.DataConversionException;
import io.xpire.model.ReadOnlyListView;
import io.xpire.model.Xpire;
import io.xpire.model.item.XpireItem;

/**
 * Represents a storage for {@link Xpire}.
 */
public interface XpireStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getXpireFilePath();

    /**
     * Returns Xpire data as a {@link ReadOnlyListView}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyListView<XpireItem>> readXpire() throws DataConversionException, IOException;

    /**
     * @see #getXpireFilePath()
     */
    Optional<ReadOnlyListView<XpireItem>> readXpire(Path filePath) throws DataConversionException,
                                                               IOException;

    /**
     * Saves the given {@link ReadOnlyListView} to the storage.
     * @param xpire cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveXpire(ReadOnlyListView<XpireItem> xpire) throws IOException;

    /**
     * @see #saveXpire(ReadOnlyListView)
     */
    void saveXpire(ReadOnlyListView<XpireItem> xpire, Path filePath) throws IOException;

}
