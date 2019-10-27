package io.xpire.storage;

import io.xpire.commons.core.LogsCenter;
import io.xpire.commons.exceptions.DataConversionException;
import io.xpire.commons.exceptions.IllegalValueException;
import io.xpire.commons.util.FileUtil;
import io.xpire.commons.util.JsonUtil;
import io.xpire.model.ReadOnlyListView;
import io.xpire.model.item.Item;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

public class JsonReplenishStorage implements ReplenishStorage {
    private static final Logger logger = LogsCenter.getLogger(JsonXpireStorage.class);

    private Path filePath;

    public JsonReplenishStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getReplenishFilePath() {
        return this.filePath;
    }

    @Override
    public Optional<ReadOnlyListView<Item>> readReplenishList() throws DataConversionException {
        return readReplenishList(this.filePath);
    }

    /**
     * Similar to {@link #readReplenishList()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyListView<Item>> readReplenishList(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableReplenishList> jsonTracker = JsonUtil.readJsonFile(
                filePath, JsonSerializableReplenishList.class);
        if (jsonTracker.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonTracker.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveReplenishList(ReadOnlyListView<Item> list) throws IOException {
        saveReplenishList(list, this.filePath);
    }

    /**
     * Similar to {@link #saveReplenishList(ReadOnlyListView)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveReplenishList(ReadOnlyListView<Item> list, Path filePath) throws IOException {
        requireNonNull(list);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableReplenishList(list), filePath);
    }

}
