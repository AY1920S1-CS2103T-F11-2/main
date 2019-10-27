package io.xpire.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import io.xpire.commons.core.LogsCenter;
import io.xpire.commons.exceptions.DataConversionException;
import io.xpire.commons.exceptions.IllegalValueException;
import io.xpire.commons.util.FileUtil;
import io.xpire.commons.util.JsonUtil;
import io.xpire.model.ReadOnlyListView;
import io.xpire.model.item.Item;

/**
 * A class to access Xpire data stored as a json file on the hard disk.
 */
public class JsonXpireStorage implements XpireStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonXpireStorage.class);

    private Path filePath;

    public JsonXpireStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getXpireFilePath() {
        return this.filePath;
    }

    @Override
    public Optional<ReadOnlyListView<? extends Item>>[] readXpire() throws DataConversionException {
        return readXpire(this.filePath);
    }

    /**
     * Similar to {@link #readXpire()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyListView<? extends Item>>[] readXpire(Path filePath) throws DataConversionException {
        requireNonNull(filePath);
        Optional<JsonSerializableList> jsonTracker = JsonUtil.readJsonFile(
                filePath, JsonSerializableList.class);
        if (jsonTracker.isEmpty()) {
            return new Optional[]{Optional.empty(), Optional.empty()};
        }

        try {
            ReadOnlyListView xpire = jsonTracker.get().toModelType()[0];
            ReadOnlyListView replenishList = jsonTracker.get().toModelType()[1];
            return new Optional[]{Optional.of(xpire), Optional.of(replenishList)};
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveXpire(ReadOnlyListView<? extends Item>[] xpire) throws IOException {
        saveXpire(xpire, this.filePath);
    }

    /**
     * Similar to {@link #saveXpire(ReadOnlyListView[])}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveXpire(ReadOnlyListView<? extends Item>[] xpire, Path filePath) throws IOException {
        requireNonNull(xpire);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableList(xpire), filePath);
    }

}
