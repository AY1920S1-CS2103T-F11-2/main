package io.xpire.model.item.sort;

import io.xpire.model.item.Item;

import java.util.Comparator;

public interface MethodOfSorting<T extends Item> {

    String SORT_NAME = "name";
    String SORT_DATE = "date";

    static boolean isValidMethodOfSorting(String test) {
        return (test.equals(SORT_NAME) || test.equals(SORT_DATE));
    }

    public Comparator<T> getComparator();

}