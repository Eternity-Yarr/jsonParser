package ru.dkom.jsonParser;

import ru.nojs.json.JSONArray;
import ru.nojs.json.JSONNull;
import ru.nojs.json.JSONObject;
import ru.nojs.json.JSONPrimitive;

import java.math.BigDecimal;
import java.math.BigInteger;

public class JSONPrimitiveClass implements JSONPrimitive{


    private Object value;

    public JSONPrimitiveClass(Object o){
        value = o;
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
        return Boolean.valueOf(value.toString());
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
        // return (Integer)value;
        return Integer.parseInt((String)value);
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
        return null;
    }

    @Override
    public JSONPrimitive getAsJsonPrimitive() {
        return this;
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
        return (String)value;
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
        return false;
    }

    @Override
    public boolean isJsonPrimitive() {
        return true;
    }
}
