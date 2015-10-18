package ru.vdovin.jsonParser;


import ru.nojs.json.*;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class MyJSONElement implements JSONElement {


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
    public JSONPrimitive getAsJsonPrimitive() {
        if (isJsonPrimitive()){
            return (MyJSONPrimitive)this;
        }
        throw new IllegalStateException("This is not s JSON primitive");
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
        return "";
    }

    @Override
    public boolean isJsonArray() {
        return false;
    }

    @Override
    public boolean isJsonPrimitive() {
        return this instanceof MyJSONPrimitive;
    }

    /////////////////////////////////////////////////////

    @Override
    public JSONObject getAsJsonObject() {
        if (isJsonObject()) {
            return (MyJSONObject) this;
        }
        throw new IllegalStateException("This id not a JSON Object: " + this);
    }

    @Override
    public JSONNull getAsJsonNull() {
        if (isJsonNull()) {
            return (MyJSONNull) this;
        }
        throw new IllegalStateException("This is not s JSON null");
    }

    @Override
    public boolean isJsonNull() {
        return this instanceof MyJSONNull;
    }

    @Override
    public boolean isJsonObject() {
        return this instanceof MyJSONObject;
    }

}
