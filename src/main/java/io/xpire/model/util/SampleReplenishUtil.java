package io.xpire.model.util;

import static io.xpire.model.util.SampleDataUtil.getTagSet;

import io.xpire.model.ReplenishList;
import io.xpire.model.item.ToBuyItem;
import io.xpire.model.item.Name;

/**
 * Contains utility methods for populating {@code ReplenishList} with sample data.
 */
public class SampleReplenishUtil {

    public static ToBuyItem[] getSampleToBuyItems() {
        return new ToBuyItem[] {
            new ToBuyItem(new Name("Twinings Earl Grey Tea"), getTagSet("Drink", "Atas")),
            new ToBuyItem(new Name("Honey Butter Almonds"), getTagSet("Imported", "Nuts"))
        };
    }

    public static ReplenishList getSampleReplenishList() {
        ReplenishList sampleReplenishList = new ReplenishList();
        for (ToBuyItem sampleItem : getSampleToBuyItems()) {
            sampleReplenishList.addItem(sampleItem);
        }
        return sampleReplenishList;
    }
}
