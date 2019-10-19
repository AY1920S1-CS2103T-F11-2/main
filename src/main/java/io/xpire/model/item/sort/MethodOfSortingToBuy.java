package io.xpire.model.item.sort;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import io.xpire.commons.util.AppUtil;
import io.xpire.model.ToBuyItem;

public class MethodOfSortingToBuy {

    public static final String MESSAGE_CONSTRAINTS = "Sorting can only be done by 'name'.";
    private final String method;

    /**
     * Constructs a {@code MethodOfSorting}.
     * @param method A valid method of sorting.
     */
    public MethodOfSortingToBuy(String method) {
        requireNonNull(method);
        AppUtil.checkArgument(isValidMethodOfToBuySorting(method), MESSAGE_CONSTRAINTS);
        this.method = method;
    }

    /**
     * Returns true if a given string is a valid method of sorting.
     */
    public static boolean isValidMethodOfToBuySorting(String test) {
        return test.equals("name");
    }

    /**
     * Returns a comparator for the given method of sorting.
     */
    public Comparator<ToBuyItem> getComparator() {
        final Comparator<ToBuyItem> nameSorter = Comparator.comparing(l->l.getName().toString(),
                String.CASE_INSENSITIVE_ORDER);
        return nameSorter;
    }

}
