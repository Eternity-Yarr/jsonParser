package ru.komrakov.jsonParser;

import ru.nojs.json.JSONArray;
import ru.nojs.json.JSONNull;
import ru.nojs.json.JSONObject;
import ru.nojs.json.JSONPrimitive;

import java.math.BigDecimal;
import java.math.BigInteger;

public class JSONElementClass implements ru.nojs.json.JSONElement {

    Object value = null;


    public JSONElementClass(Object jsonObject){
        value = jsonObject;
    }

    @Override
    public BigDecimal getAsBigDecimal() {
        return ((JSONPrimitiveClass)value).getAsBigDecimal();
    }

    @Override
    public BigInteger getAsBigInteger() {
        return ((JSONPrimitiveClass)value).getAsBigInteger();
    }

    @Override
    public boolean getAsBoolean() {
        return ((JSONPrimitiveClass)value).getAsBoolean();
    }

    @Override
    public byte getAsByte() {
        return ((JSONPrimitiveClass)value).getAsByte();
    }

    @Override
    public char getAsCharacter() {
        return ((JSONPrimitiveClass)value).getAsCharacter();
    }

    @Override
    public double getAsDouble() {
        return ((JSONPrimitiveClass)value).getAsDouble();
    }

    @Override
    public float getAsFloat() {
        return ((JSONPrimitiveClass)value).getAsFloat();
    }

    @Override
    public int getAsInt() {
        return ((JSONPrimitiveClass)value).getAsInt();
    }

    @Override
    public JSONArray getAsJsonArray() {
        return (JSONArrayClass)value;
    }

    @Override
    public JSONNull getAsJsonNull() {
        return new JSONNullClass();
    }

    @Override
    public JSONObject getAsJsonObject() {
        return (JSONObjectClass)value;
    }

    @Override
    public JSONPrimitive getAsJsonPrimitive() {
        return (JSONPrimitiveClass)value;
    }

    @Override
    public long getAsLong() {
        return ((JSONPrimitiveClass)value).getAsLong();
    }

    @Override
    public Number getAsNumber() {
        return ((JSONPrimitiveClass)value).getAsNumber();
    }

    @Override
    public short getAsShort() {
        return ((JSONPrimitiveClass)value).getAsShort();
    }

    @Override
    public String getAsString() {
        return ((JSONPrimitiveClass)value).getAsString();
    }

    @Override
    public boolean isJsonArray() {
        return ((JSONArrayClass)value).isJsonArray();
    }

    @Override
    public boolean isJsonNull() {
        return ((JSONPrimitiveClass)value).isJsonNull();
    }

    @Override
    public boolean isJsonObject() {
        return ((JSONObjectClass)value).isJsonObject();

    }

    @Override
    public boolean isJsonPrimitive() {
        return ((JSONPrimitiveClass)value).isJsonPrimitive();
    }

    //FIXME: Cleanup
    private boolean isNull(Object o){
        if (o == null){
            return true;
        }
        return false;
    }
}
