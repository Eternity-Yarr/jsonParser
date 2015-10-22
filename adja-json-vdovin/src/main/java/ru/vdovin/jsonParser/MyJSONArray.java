package ru.vdovin.jsonParser;

import ru.nojs.json.JSONArray;
import ru.nojs.json.JSONElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyJSONArray extends MyJSONElement implements JSONArray {

   private List<JSONElement> elements = new ArrayList<>();

    @Override
    public void add(JSONElement element) {
        elements.add(element);
    }

    @Override
    public void addAll(JSONArray array) {

    }

    @Override
    public boolean contains(JSONElement element) {
        return false;
    }

    @Override
    public JSONElement get(int i) {
        return elements.get(i);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<JSONElement> iterator() {
        return null;
    }
}
