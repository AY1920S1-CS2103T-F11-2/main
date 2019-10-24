package io.xpire.model.item;

import io.xpire.model.item.sort.SortingMethod;
import javafx.collections.ObservableList;

import java.util.List;

public interface SortedUniqueList<T> extends Iterable<T> {

    boolean contains(T toCheck);

    void add(T toAdd);

    void setItem(T targetItem, T editedItem);

    void remove(T toRemove);

    void setItems(SortedUniqueList<T> replacement);

    void setItems(List<T> items);

    void setMethodOfSorting(SortingMethod<T> method);

    ObservableList<T> asUnmodifiableObservableList();

}