package ru.nojs.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Created by Юыху on 20.10.2015.
 */
public class JSONPrimitiveImpl implements JSONPrimitive {
    private JSONArrayImpl jar;
    private Object obj;
    private static final Class<?>[] PRIMITIVE_TYPES = { int.class, long.class, short.class,
            float.class, double.class, byte.class, boolean.class, char.class, Integer.class, Long.class,
            Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class };

    public JSONPrimitiveImpl (char character){
        setValue(character);
    }

    public JSONPrimitiveImpl (int integer){
        setValue(integer);
    }

    public JSONPrimitiveImpl (String string){
        setValue(string);
    }

    public JSONPrimitiveImpl(Object object) {
        setValue(object);
    }

    void setValue(Object value){
        if (value instanceof Character){
            char c = ((Character) value).charValue();
            this.obj=String.valueOf(c);
        } else {
            if ((value instanceof Number) || isTypeValue(value)) {
                this.obj = value;
            }
        }
    }

    boolean isTypeValue(Object object){
        if (object instanceof String){
            return true;
        }

        Class<?> classPrim = object.getClass();
        for (Class<?> primitive: PRIMITIVE_TYPES) {
            if (primitive.isAssignableFrom(classPrim)){
                return true;
            }
        }
        return false;
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
        if (isBoolean()){
            return Boolean.parseBoolean(getAsString());
        } else
            throw new IllegalStateException("This is not boolean type");
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
        return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
    }

    @Override
    public JSONArray getAsJsonArray() {
        if (isJsonArray()) {
            return (JSONArray) this;
        }
        throw new IllegalStateException("This Json not JsonArray");
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
        if (isJsonPrimitive()) {
            return (JSONPrimitive) this;
        }
        throw new IllegalStateException("This Json not JsonPrimitive");
    }

    @Override
    public long getAsLong() {
        return 0;
    }

    @Override
    public Number getAsNumber() {
        return (Number) obj;
    }

    @Override
    public short getAsShort() {
        return 0;
    }

    @Override
    public String getAsString() {
        if (isNumber()){
            return getAsNumber().toString();
        } else if (isBoolean()){
            return getAsBooleanWrapper().toString();
        }
        return (String) obj;
    }

    @Override
    public boolean isJsonArray() {
        return this instanceof JSONArray;
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

    public boolean isNumber() {
        return obj instanceof Number;
    }

    public boolean isBoolean() {
        return obj instanceof Boolean;
    }

    Boolean getAsBooleanWrapper() {
        return (Boolean) obj;
    }
}
