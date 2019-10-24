package io.xpire.model.item.sort;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import io.xpire.commons.util.AppUtil;
import io.xpire.model.item.Item;
import io.xpire.model.item.SortedUniqueList;

/**
 * Represents a MethodOfSorting in the expiry date tracker.
 * Guarantees: immutable; name is valid as declared in {@link #isValidMethodOfSorting(String)}
 */
public class MethodOfSorting implements SortingMethod<Item> {

    public static final String MESSAGE_CONSTRAINTS = "Sorting can only be done by 'name' or 'date'.";
    private final Comparator<Item> nameSorter = Comparator.comparing(l->l.getName().toString(),
            String.CASE_INSENSITIVE_ORDER);
    private final Comparator<Item> dateSorter = Comparator.comparing(l->l.getExpiryDate().getDate(),
            Comparator.nullsFirst(Comparator.naturalOrder()));
    private final Comparator<Item> nameThenDateSorter = nameSorter.thenComparing(dateSorter);
    private final String method;

    /**
     * Constructs a {@code MethodOfSorting}.
     * @param method A valid method of sorting.
     */
    public MethodOfSorting(String method) {
        requireNonNull(method);
        AppUtil.checkArgument(SortingMethod.isValidMethodOfSorting(method), MESSAGE_CONSTRAINTS);
        this.method = method;
    }

    /**
     * Returns a comparator for the given method of sorting.
     */
    public Comparator<Item> getComparator() {
        switch (method) {
        case "date":
            return dateSorter;
        default:
            return nameThenDateSorter;
        }
    }



    /**
     * Returns the string value of the method of sorting.
     * @return The string representation of the method of sorting.
     */
    @Override
    public String toString() {
        return this.method;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof MethodOfSorting)) {
            return false;
        } else {
            MethodOfSorting other = (MethodOfSorting) obj;
            return this.method.equals(other.method);
        }
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }
}
