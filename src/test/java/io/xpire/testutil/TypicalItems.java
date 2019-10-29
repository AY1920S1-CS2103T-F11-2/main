package io.xpire.testutil;

import static io.xpire.model.item.ExpiryDate.DATE_FORMAT;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.xpire.commons.util.DateUtil;
import io.xpire.model.ReadOnlyListView;
import io.xpire.model.ReplenishList;
import io.xpire.model.Xpire;
import io.xpire.model.item.Item;
import io.xpire.model.item.XpireItem;

/**
 * A utility class containing a list of {@code XpireItem} objects to be used in tests.
 */
public class TypicalItems {

    public static final String TODAY = DateUtil.convertDateToString(LocalDate.now(), DATE_FORMAT);
    public static final String IN_A_WEEK = DateUtil.convertDateToString(LocalDate.now().plusDays(7), DATE_FORMAT);
    public static final String IN_TWO_WEEKS = DateUtil.convertDateToString(LocalDate.now().plusDays(14), DATE_FORMAT);
    public static final String IN_A_MONTH = DateUtil.convertDateToString(LocalDate.now().plusDays(30), DATE_FORMAT);
    public static final String PASSED_A_DAY = DateUtil.convertDateToString(LocalDate.now().minusDays(1), DATE_FORMAT);
    public static final String PASSED_A_WEEK = DateUtil.convertDateToString(LocalDate.now().minusDays(7), DATE_FORMAT);

    // ====================== Xpire Items ==========================================================================

    public static final XpireItem BANANA = new XpireItemBuilder().withName("Banana")
                                            .withExpiryDate(IN_TWO_WEEKS)
                                            .withQuantity("5").build();



    public static final XpireItem KIWI = new XpireItemBuilder().withName("Kiwi")
                                                      .withExpiryDate(IN_A_MONTH)
                                                      .withQuantity("2")
                                                      .withThreshold("20").build();

    //with tags
    public static final XpireItem DUCK = new XpireItemBuilder().withName("Duck")
                                                     .withExpiryDate(IN_A_MONTH)
                                                     .withTags("Fridge", "Protein")
                                                     .build();

    //with all fields (tags, reminder threshold, quantity)
    public static final XpireItem JELLY = new XpireItemBuilder().withName("Jelly")
                                                        .withExpiryDate(IN_A_MONTH)
                                                        .withQuantity("4")
                                                        .withTags("Fridge")
                                                        .withReminderThreshold("3")
                                                        .build();

    // expiring soon
    public static final XpireItem EXPIRING_FISH = new XpireItemBuilder().withName("Fish")
                                                               .withExpiryDate(IN_A_WEEK)
                                                               .withQuantity("1")
                                                               .withThreshold("8").build();
    // already expired
    public static final XpireItem EXPIRED_APPLE = new XpireItemBuilder().withName("Apple")
                                                              .withExpiryDate(TODAY)
                                                              .withQuantity("1").build();

    public static final XpireItem EXPIRED_ORANGE = new XpireItemBuilder().withName("Orange")
                                                             .withExpiryDate(PASSED_A_DAY)
                                                             .withQuantity("1").build();

    // expired for a longer time
    public static final XpireItem EXPIRED_MILK = new XpireItemBuilder().withName("Milk")
                                                             .withExpiryDate(PASSED_A_WEEK)
                                                             .withQuantity("2").build();


    //====================== Replenish List Items ====================================================

    public static final Item BAGEL = new ItemBuilder().withName("Bagel").build();

    public static final Item CHOCOLATE = new ItemBuilder().withName("Chocolate")
                                            .withTags("Cadbury", "Cocoa").build();

    private TypicalItems() {} // prevents instantiation

    /**
     * Returns an {@code Xpire} with all the typical items.
     */
    public static ReadOnlyListView<? extends Item>[] getTypicalLists() {
        Xpire edt = new Xpire();
        for (XpireItem xpireItem : getTypicalXpireItems()) {
            XpireItem copyXpireItem = new XpireItem(xpireItem);
            edt.addItem(copyXpireItem);
        }
        ReplenishList replenishList = new ReplenishList();
        return new ReadOnlyListView[]{edt, replenishList};
    }

    /**
     * Returns an {@code Xpire} with all the typical items.
     */
    public static Xpire getTypicalExpiryDateTracker() {
        Xpire edt = new Xpire();
        for (XpireItem xpireItem : getTypicalXpireItems()) {
            XpireItem copyXpireItem = new XpireItem(xpireItem);
            edt.addItem(copyXpireItem);
        }
        return new Xpire(edt);
    }

    public static List<XpireItem> getTypicalXpireItems() {
        return new ArrayList<>(Arrays.asList(EXPIRED_MILK, BANANA, EXPIRED_APPLE, EXPIRED_ORANGE, EXPIRING_FISH, DUCK,
                JELLY));

    }

    public static List<Item> getTypicalItems() {
        return new ArrayList<>(Arrays.asList(BAGEL, CHOCOLATE));

    }

}
