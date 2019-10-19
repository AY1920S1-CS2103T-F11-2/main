package io.xpire;

import io.xpire.model.Model;

public class ItemManager {

    private final Model model;

    ItemManager(Model model) {
        this.model = model;
    }

    public void updateItemTags() {
        model.updateItemTags();
    }

}
