package ru.dkom.jsonParser.MyJSONClasses;

import ru.dkom.jsonParser.JSONPrimitiveClass;
import ru.nojs.json.JSONArray;
import ru.nojs.json.JSONNull;
import ru.nojs.json.JSONObject;
import ru.nojs.json.JSONPrimitive;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by User on 08.11.2015.
 */
public class MyJSONPrimitive implements JSONPrimitive{

    JSONPrimitive jsonPrimitive;

    @Override
    public boolean equals(Object primitive){
        if (jsonPrimitive.getAsString().equals(((JSONPrimitive)primitive).getAsString())){
            return true;
        }
        return false;
    }

    public MyJSONPrimitive(Object o){
        jsonPrimitive = new JSONPrimitiveClass(o);
    }

    @Override
    public BigDecimal getAsBigDecimal() {
        return jsonPrimitive.getAsBigDecimal();
    }

    @Override
    public BigInteger getAsBigInteger() {
        return jsonPrimitive.getAsBigInteger();
    }

    @Override
    public boolean getAsBoolean() {
        return jsonPrimitive.getAsBoolean();
    }

    @Override
    public byte getAsByte() {
        return jsonPrimitive.getAsByte();
    }

    @Override
    public char getAsCharacter() {
        return jsonPrimitive.getAsCharacter();
    }

    @Override
    public double getAsDouble() {
        return jsonPrimitive.getAsDouble();
    }

    @Override
    public float getAsFloat() {
        return jsonPrimitive.getAsFloat();
    }

    @Override
    public int getAsInt() {
        return jsonPrimitive.getAsInt();
    }

    @Override
    public JSONArray getAsJsonArray() {
        return jsonPrimitive.getAsJsonArray();
    }

    @Override
    public JSONNull getAsJsonNull() {
        return jsonPrimitive.getAsJsonNull();
    }

    @Override
    public JSONObject getAsJsonObject() {
        return jsonPrimitive.getAsJsonObject();
    }

    @Override
    public JSONPrimitive getAsJsonPrimitive() {
        return jsonPrimitive.getAsJsonPrimitive();
    }

    @Override
    public long getAsLong() {
        return jsonPrimitive.getAsLong();
    }

    @Override
    public Number getAsNumber() {
        return jsonPrimitive.getAsNumber();
    }

    @Override
    public short getAsShort() {
        return jsonPrimitive.getAsShort();
    }

    @Override
    public String getAsString() {
        return jsonPrimitive.getAsString();
    }

    @Override
    public boolean isJsonArray() {
        return jsonPrimitive.isJsonArray();
    }

    @Override
    public boolean isJsonNull() {
        return jsonPrimitive.isJsonNull();
    }

    @Override
    public boolean isJsonObject() {
        return jsonPrimitive.isJsonObject();
    }

    @Override
    public boolean isJsonPrimitive() {
        return jsonPrimitive.isJsonPrimitive();
    }
}
