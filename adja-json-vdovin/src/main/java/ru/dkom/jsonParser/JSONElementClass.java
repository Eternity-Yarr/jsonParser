package ru.dkom.jsonParser;

import ru.nojs.json.JSONArray;
import ru.nojs.json.JSONNull;
import ru.nojs.json.JSONObject;
import ru.nojs.json.JSONPrimitive;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public class JSONElementClass implements ru.nojs.json.JSONElement {

    JSONArrayClass array = null;
    JSONObjectClass object = null;
    JSONPrimitiveClass primitive = null;

    public JSONElementClass(JSONArray array){
        this.array = (JSONArrayClass)array;
    }

    public JSONElementClass(JSONObject object){
        this.object = (JSONObjectClass)object;
    }

    public JSONElementClass(JSONPrimitive primitive){
        this.primitive = (JSONPrimitiveClass)primitive;
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
        return primitive.getAsInt();
    }

    @Override
    public JSONArray getAsJsonArray() {
        return array;
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
        return !isNull(array);
    }

    @Override
    public boolean isJsonNull() {
        return false;
    }

    @Override
    public boolean isJsonObject() {
        return !isNull(object);
    }

    @Override
    public boolean isJsonPrimitive() {
        return false;
    }

    private boolean isNull(Object o){
        if (o == null){
            return true;
        }
        return false;
    }
}
