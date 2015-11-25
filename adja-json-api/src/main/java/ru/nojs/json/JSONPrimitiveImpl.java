package ru.nojs.json;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Created by ���� on 20.10.2015.
 */
public class JSONPrimitiveImpl implements JSONPrimitive {
    private Object obj;

    public JSONPrimitiveImpl (Number number){
        setValue(number);
    }

    public JSONPrimitiveImpl (String string){
        setValue(string);
    }

    public JSONPrimitiveImpl (Boolean bl) {
        setValue(bl);
    }

    public JSONPrimitiveImpl(Object object) {
        setValue(object);
    }

    void setValue(Object value){
        obj = value;
    }

    boolean isTypeValue(Object object){
        if (object instanceof String){
            return true;
        } else if (object instanceof Boolean){
            return true;
        }

        return object.getClass().isPrimitive();
    }

    @Override
    public String toString(){
        return obj.toString();
    }

    @Override
    public boolean equals(Object obj){
        return new EqualsBuilder().reflectionEquals(this, obj);
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder().reflectionHashCode(this);
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
            return ((Boolean) obj).booleanValue();
        } else if (obj.equals("true")||obj.equals("false")){
            return Boolean.parseBoolean(obj.toString());
        } else {
            throw new IllegalStateException("This is not boolean type");
        }
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
        //FIXME: загадка какая-то вообще :)
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
            return ((Boolean) obj).toString();
        }
        return obj.toString();
    }

    @Override
    public boolean isJsonArray() {
        //FIXME: как примитив может быть instanceof чего либо другого?
        return this instanceof JSONArray;
    }

    @Override
    public boolean isJsonNull() {
        return this instanceof JSONNull;
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
}
