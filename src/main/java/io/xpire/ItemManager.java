package io.xpire;

import static io.xpire.logic.LogicManager.FILE_OPS_ERROR_MESSAGE;

import java.io.IOException;
import java.util.logging.Logger;

import io.xpire.commons.core.LogsCenter;
import io.xpire.model.Model;
import io.xpire.storage.Storage;

public class ItemManager {

    private static final Logger logger = LogsCenter.getLogger(ItemManager.class);

    private final Model model;
    private final Storage storage;

    ItemManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
    }

    public void updateItemTags() {
        model.updateItemTags();
        try {
            this.storage.saveXpire(this.model.getXpire());
        } catch (IOException ioe) {
            logger.info(FILE_OPS_ERROR_MESSAGE + ioe);
        }
    }

}
