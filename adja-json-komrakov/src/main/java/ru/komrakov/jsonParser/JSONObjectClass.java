package ru.komrakov.jsonParser;

import ru.nojs.json.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JSONObjectClass implements JSONObject{

    private Map<String,Object> properties;
    //FIXME: Cleanup!
    private Set<String> propertiesNames;

    public JSONObjectClass(){
        properties = new HashMap<>();
    }

    @Override
    public void add(String property, JSONElement value) {
        //properties.put(property,new JSONPrimitiveClass(value));
        properties.put(property, value);
    }

    @Override
    public void addProperty(String property, Boolean value) {
        properties.put(property,value);
    }

    @Override
    public void addProperty(String property, Number value) {
        properties.put(property,value);
    }

    @Override
    public void addProperty(String property, String value) {
        properties.put(property,new JSONPrimitiveClass(value));
    }

    @Override
    public Set<Map.Entry<String, JSONElement>> entrySet() {
        return null;
    }

    @Override
    public JSONElement get(String memberName) {

        return (JSONElement)properties.get(memberName);
    }

    @Override
    public JSONArray getAsJsonArray(String memberName) {
        return null;
    }

    @Override
    public JSONObject getAsJsonObject(String memberName) {
        return (JSONObject)properties.get(memberName);
    }

    @Override
    public JSONPrimitive getAsJsonPrimitive(String memberName) {
        return (JSONPrimitive)properties.get(memberName);
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
    public BigDecimal getAsBigDecimal() {
        return null;
    }

    @Override
    public BigInteger getAsBigInteger() {
        return null;
    }

    @Override
    public boolean getAsBoolean() {
        return false;
    }

    @Override
    public byte getAsByte() {
        return 0;
    }

    @Override
    public char getAsCharacter() {
        return 0;
    }

    @Override
    public double getAsDouble() {
        return 0;
    }

    @Override
    public float getAsFloat() {
        return 0;
    }

    @Override
    public int getAsInt() {
        return 0;
    }

    @Override
    public JSONArray getAsJsonArray() {
        return null;
    }

    @Override
    public JSONNull getAsJsonNull() {
        return null;
    }

    @Override
    public JSONObject getAsJsonObject() {
        //return null;
        return this;
    }

    @Override
    public JSONPrimitive getAsJsonPrimitive() {
        return null;
    }

    @Override
    public long getAsLong() {
        return 0;
    }

    @Override
    public Number getAsNumber() {
        return null;
    }

    @Override
    public short getAsShort() {
        return 0;
    }

    @Override
    public String getAsString() {
        return null;
    }

    @Override
    public boolean isJsonArray() {
        return false;
    }

    @Override
    public boolean isJsonNull() {
        return false;
    }

    @Override
    public boolean isJsonObject() {
        return true;
        /*
        if (properties.size() > 0){
            return true;
        }
        return false;*/
    }

    @Override
    public boolean isJsonPrimitive() {
        return false;
    }
}
