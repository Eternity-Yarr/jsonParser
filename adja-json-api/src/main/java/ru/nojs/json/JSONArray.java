package ru.nojs.json;

public interface JSONArray extends JSONElement, Iterable<JSONElement> {
    // Adds the specified element to self.
    void add(JSONElement element);
    // Adds all the elements of the specified array to self.
    void addAll(JSONArray array);
    // Returns true if this array contains the specified element.
    boolean contains(JSONElement element);
    // Returns the ith element of the array.
    JSONElement get(int i);
}
