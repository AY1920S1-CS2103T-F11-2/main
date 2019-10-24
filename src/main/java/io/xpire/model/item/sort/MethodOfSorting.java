package io.xpire.model.item.sort;

import java.util.Comparator;

public interface MethodOfSorting<T> {

    String SORT_NAME = "name";
    String SORT_DATE = "date";

    static boolean isValidMethodOfSorting(String test) {
        return (test.equals(SORT_NAME) || test.equals(SORT_DATE));
    }

    public Comparator<T> getComparator();

}
