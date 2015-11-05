package ru.vdovin.jsonParser;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import ru.nojs.json.JSONArray;
import ru.nojs.json.JSONElement;

import java.lang.reflect.Array;
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
        return elements.contains(element);
    }

    @Override
    public JSONElement get(int i) {
        if ( i < 0 || i >= elements.size() ){
            throw new IndexOutOfBoundsException();
        }
        return elements.get(i);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<JSONElement> iterator() {
        return elements.iterator();
    }

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder()
                .reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(this)
                .toHashCode();
    }

}
