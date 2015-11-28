package ru.nojs.json;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


//FIXME: унеси в свой пакет
/**
 * Created by ���� on 17.10.2015.
 */
public class JSONArrayImpl implements JSONArray {
    private final List<JSONElement> list = new ArrayList<>();

    @Override
    public void add(JSONElement element) {
        list.add(element);
    }

    @Override
    public void addAll(JSONArray array) {

    }

    @Override
    public boolean contains(JSONElement element) {
        return false;
    }

    @Override
    public JSONElement get(int i) {
        return null;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Iterator<JSONElement> iterator() {
        return list.iterator();
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
        if (list.size() == 1){
            return list.get(0).getAsInt();
        }
        throw new IllegalStateException();
    }

    @Override
    public JSONArray getAsJsonArray() {
            return this;
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
        return true;
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
        return false;
    }

    @Override
    public boolean equals (Object obj){
        return new EqualsBuilder().reflectionEquals(this, obj);
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder().reflectionHashCode(this);
    }


}
