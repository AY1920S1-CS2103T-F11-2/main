package io.xpire.model.util;

import io.xpire.model.ReadOnlyXpire;
import io.xpire.model.ReplenishList;
import io.xpire.model.ToBuyItem;
import io.xpire.model.Xpire;
import io.xpire.model.item.ExpiryDate;
import io.xpire.model.item.Item;
import io.xpire.model.item.Name;
import io.xpire.model.item.Quantity;

import static io.xpire.model.util.SampleDataUtil.getTagSet;

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
