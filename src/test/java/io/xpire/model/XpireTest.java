package io.xpire.model;

import static io.xpire.logic.CommandParserItemUtil.VALID_EXPIRY_DATE_APPLE;
import static io.xpire.logic.CommandParserItemUtil.VALID_TAG_FRUIT;
import static io.xpire.testutil.Assert.assertThrows;
import static io.xpire.testutil.TypicalItems.EXPIRED_APPLE;
import static io.xpire.testutil.TypicalItems.getTypicalExpiryDateTracker;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.xpire.model.item.Item;
import io.xpire.model.item.exceptions.DuplicateItemException;
import io.xpire.testutil.ItemBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class XpireTest {

    private final Xpire xpire = new Xpire();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), xpire.getItemList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> xpire.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyExpiryDateTracker_replacesData() {
        Xpire newData = getTypicalExpiryDateTracker();
        xpire.resetData(newData);
        assertEquals(newData.getItemList(), xpire.getItemList());
    }

    @Test
    public void resetData_withDuplicateItems_throwsDuplicateItemException() {
        Item editedApple = new ItemBuilder(EXPIRED_APPLE).withExpiryDate(VALID_EXPIRY_DATE_APPLE)
                                                         .withQuantity("1").build();
        List<Item> newItems = Arrays.asList(EXPIRED_APPLE, editedApple);
        XpireStub newData = new XpireStub(newItems);
        assertThrows(DuplicateItemException.class, () -> xpire.resetData(newData));
    }

    @Test
    public void hasItem_nullItem_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> xpire.hasItem(null));
    }

    @Test
    public void hasItem_itemNotInExpiryDateTracker_returnsFalse() {
        assertFalse(xpire.hasItem(EXPIRED_APPLE));
    }

    @Test
    public void hasItem_itemInExpiryDateTracker_returnsTrue() {
        xpire.addItem(EXPIRED_APPLE);
        assertTrue(xpire.hasItem(EXPIRED_APPLE));
    }

    @Test
    public void hasItem_itemWithSameIdentityFieldsInExpiryDateTracker_returnsTrue() {
        xpire.addItem(EXPIRED_APPLE);
        Item editedAlice = new ItemBuilder(EXPIRED_APPLE)
                .withExpiryDate(VALID_EXPIRY_DATE_APPLE)
                .withTags(VALID_TAG_FRUIT)
                .build();
        assertTrue(xpire.hasItem(editedAlice));
    }

    @Test
    public void getItemList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> xpire.getItemList().remove(0));
    }

    /**
     * A stub ReadOnlyXpire whose items list can violate interface constraints.
     */
    private static class XpireStub implements ReadOnlyXpire {
        private final ObservableList<Item> items = FXCollections.observableArrayList();

        XpireStub(Collection<Item> items) {
            this.items.setAll(items);
        }

        @Override
        public ObservableList<Item> getItemList() {
            return items;
        }
    }

}
