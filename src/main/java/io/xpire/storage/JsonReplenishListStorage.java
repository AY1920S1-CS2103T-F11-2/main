package io.xpire.storage;

import io.xpire.commons.core.LogsCenter;
import io.xpire.commons.util.FileUtil;
import io.xpire.commons.util.JsonUtil;
import io.xpire.model.ReplenishList;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

public class JsonReplenishListStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonReplenishListStorage.class);

    private Path filePath;

    public JsonReplenishListStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getReplenishListFilePath() {
        return this.filePath;
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
