package io.xpire.storage;

import io.xpire.commons.exceptions.DataConversionException;
import io.xpire.model.ReplenishList;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public interface ReplenishListStorage {

    Path getReplenishListFilePath();

    void saveReplenishList(ReplenishList replenishList) throws IOException;

    void saveReplenishList(ReplenishList replenishList, Path filePath) throws IOException;

    Optional<ReplenishList> readReplenishList() throws DataConversionException, IOException;

    Optional<ReplenishList> readReplenishList(Path filePath) throws DataConversionException, IOException;
}
