package ru.nojs.json;

import java.util.Collection;

public interface JSONArray extends JSONElement, Iterable<JSONElement> {
    // Adds the specified element to self.
    void add(JSONElement element);
    // Adds all the elements of the specified array to self.
    void addAll(JSONArray array);
    // Returns true if this array contains the specified element.
    boolean contains(JSONElement element);
    // Returns the ith element of the array.
    JSONElement get(int i);
    // Returns size of array.
    int size();
}
