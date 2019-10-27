package io.xpire.model.item.sort;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.xpire.testutil.Assert;

public class XpireMethodOfSortingTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new XpireMethodOfSorting(null));
    }

    @Test
    public void constructor_invalidMethodOfSorting_throwsIllegalArgumentException() {
        String invalidMethodOfSorting = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new XpireMethodOfSorting(invalidMethodOfSorting));
    }

    @Test
    public void isValidMethodOfSorting() {
        // null name
        Assert.assertThrows(NullPointerException.class, () -> XpireMethodOfSorting.isValidMethodOfSorting(null));

        // invalid method of sorting
        assertFalse(XpireMethodOfSorting.isValidMethodOfSorting("")); // empty string
        assertFalse(XpireMethodOfSorting.isValidMethodOfSorting(" ")); // spaces only
        assertFalse(XpireMethodOfSorting.isValidMethodOfSorting("^")); // only name or date
        assertFalse(XpireMethodOfSorting.isValidMethodOfSorting("3")); // only name or date

        // valid method of sorting, but wrong case
        assertFalse(XpireMethodOfSorting.isValidMethodOfSorting("Name")); // should be "name"
        assertFalse(XpireMethodOfSorting.isValidMethodOfSorting("Date")); // should be "date"

        // valid method of sorting, either name or date
        assertTrue(XpireMethodOfSorting.isValidMethodOfSorting("name")); // name
        assertTrue(XpireMethodOfSorting.isValidMethodOfSorting("date")); // date
    }
}
