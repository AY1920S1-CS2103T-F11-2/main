package io.xpire.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import io.xpire.commons.exceptions.DataConversionException;
import io.xpire.model.ReplenishList;

/**
 * Represents a storage for {@link ReplenishList}.
 */
public interface ReplenishListStorage {

    Path getReplenishListFilePath();

    void saveReplenishList(ReplenishList replenishList) throws IOException;

    void saveReplenishList(ReplenishList replenishList, Path filePath) throws IOException;

    Optional<ReplenishList> readReplenishList() throws DataConversionException, IOException;

    Optional<ReplenishList> readReplenishList(Path filePath) throws DataConversionException, IOException;
}
