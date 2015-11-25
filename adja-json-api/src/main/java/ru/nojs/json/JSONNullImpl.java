package ru.nojs.json;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by ���� on 01.11.2015.
 */
public class JSONNullImpl implements JSONNull{

    //FIXME: а это зачем?
    public static final JSONNullImpl getInstance(){
        return new JSONNullImpl();
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
        return false;
    }

    @Override
    //FIXME: return true;
    public boolean isJsonNull() {
        return this instanceof JSONNull;
    }

    @Override
    public boolean isJsonObject() {
        return false;
    }

    @Override
    public boolean isJsonPrimitive() {
        return false;
    }
}
