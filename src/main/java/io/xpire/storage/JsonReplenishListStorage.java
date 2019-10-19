package io.xpire.storage;

import io.xpire.commons.core.LogsCenter;
import io.xpire.commons.exceptions.DataConversionException;
import io.xpire.commons.exceptions.IllegalValueException;
import io.xpire.commons.util.FileUtil;
import io.xpire.commons.util.JsonUtil;
import io.xpire.model.ReplenishList;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

public class JsonReplenishListStorage implements ReplenishListStorage{

    private static final Logger logger = LogsCenter.getLogger(JsonReplenishListStorage.class);

    private Path filePath;

    public JsonReplenishListStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getReplenishListFilePath() {
        return this.filePath;
    }

    @Override
    public Optional<ReplenishList> readReplenishList() throws DataConversionException {
        return readReplenishList(this.filePath);
    }


    public Optional<ReplenishList> readReplenishList(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableReplenishList> jsonTracker = JsonUtil.readJsonFile(
                filePath, JsonSerializableReplenishList.class);
        if (jsonTracker.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonTracker.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in Replenish List " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    public void saveReplenishList(ReplenishList replenishList) throws IOException {
        saveReplenishList(replenishList, this.filePath);
    }

    public void saveReplenishList(ReplenishList replenishList, Path filePath) throws IOException {
        requireNonNull(replenishList);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableReplenishList(replenishList), filePath);
    }

}
