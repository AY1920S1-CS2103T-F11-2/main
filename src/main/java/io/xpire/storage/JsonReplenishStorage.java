package io.xpire.storage;

import io.xpire.commons.core.LogsCenter;
import io.xpire.commons.exceptions.DataConversionException;
import io.xpire.commons.exceptions.IllegalValueException;
import io.xpire.commons.util.FileUtil;
import io.xpire.commons.util.JsonUtil;
import io.xpire.model.ReadOnlyListView;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

public class JsonReplenishStorage implements ListStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonXpireStorage.class);

    private Path filePath;

    public JsonReplenishStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getListFilePath() {
        return this.filePath;
    }

    @Override
    public Optional<ReadOnlyListView> readList() throws DataConversionException {
        return readList(this.filePath);
    }

    /**
     * Similar to {@link #readList()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyListView> readList(Path filePath) throws DataConversionException {
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
    public void saveList(ReadOnlyListView xpire) throws IOException {
        saveList(xpire, this.filePath);
    }

    /**
     * Similar to {@link #saveList(ReadOnlyListView)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveList(ReadOnlyListView xpire, Path filePath) throws IOException {
        requireNonNull(xpire);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableReplenishList(xpire), filePath);
    }

}
