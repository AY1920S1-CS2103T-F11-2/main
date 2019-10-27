package io.xpire.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import io.xpire.commons.exceptions.DataConversionException;
import io.xpire.model.ReadOnlyListView;
import io.xpire.model.ReadOnlyUserPrefs;
import io.xpire.model.UserPrefs;
import io.xpire.model.item.Item;

/**
 * API of the Storage component
 */
public interface Storage extends XpireStorage, ReplenishStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getXpireFilePath();

    @Override
    Optional<ReadOnlyListView<? extends Item>>[] readXpire() throws DataConversionException, IOException;

    @Override
    void saveXpire(ReadOnlyListView<? extends Item>[] xpire) throws IOException;

}
