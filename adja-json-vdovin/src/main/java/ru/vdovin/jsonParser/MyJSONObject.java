package ru.vdovin.jsonParser;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import ru.nojs.json.JSONArray;
import ru.nojs.json.JSONElement;
import ru.nojs.json.JSONObject;
import ru.nojs.json.JSONPrimitive;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class MyJSONObject extends MyJSONElement implements JSONObject {

    private final HashMap<String, JSONElement> members = new LinkedHashMap<>();

    @Override
    public void add(String property, JSONElement value) {
        if (value == null) {
            value = MyJSONNull.INSTANCE;
        }

        members.put(property,value);

    }

    private MyJSONElement createJsonElement(Object value) {
        if (value == null) {
            return MyJSONNull.INSTANCE;
        } else {
            return new MyJSONPrimitive(value);
        }
    }

    @Override
    public void addProperty(String property, Boolean value) {
        add(property, createJsonElement(value));
    }

    @Override
    public void addProperty(String property, Number value) {
        add(property, createJsonElement(value));
    }

    @Override
    public void addProperty(String property, String value) {
        add(property, createJsonElement(value));
    }

    @Override
    public Set<Map.Entry<String, JSONElement>> entrySet() {
        return members.entrySet();
    }

    @Override
    public JSONElement get(String memberName) {
        return members.get(memberName);
    }

    @Override
    public JSONArray getAsJsonArray(String memberName) {
        return super.getAsJsonArray();
    }

    @Override
    public JSONObject getAsJsonObject(String memberName) {
        return super.getAsJsonObject();
    }

    @Override
    public JSONPrimitive getAsJsonPrimitive(String memberName) {
        if (members.get(memberName).isJsonPrimitive()){
            return (MyJSONPrimitive)members.get(memberName);
        }
        throw new IllegalStateException("Is is not a primitive");
    }

    @Override
    public boolean has(String memberName) {
        return false;
    }

    @Override
    public JSONElement remove(String property) {
        return null;
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
