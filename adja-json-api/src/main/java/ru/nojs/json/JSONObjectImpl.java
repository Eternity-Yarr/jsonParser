package ru.nojs.json;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Юыху on 01.11.2015.
 */
public class JSONObjectImpl implements JSONObject {
    private final Map<String, JSONElement> map= new LinkedHashMap<>();

    @Override
    public void add(String property, JSONElement obj) {
        map.put(property, obj);
    }

    @Override
    public void addProperty(String property, Boolean value) {

    }

    @Override
    public void addProperty(String property, Number value) {

    }

    @Override
    public void addProperty(String property, String value) {
    }

    @Override
    public Set<Map.Entry<String, JSONElement>> entrySet() {
        return null;
    }

    @Override
    public JSONElement get(String memberName) {
        return map.get(memberName);
    }

    @Override
    public JSONArray getAsJsonArray(String memberName) {
        return null;
    }

    @Override
    public JSONObject getAsJsonObject(String memberName) {
        return null;
    }

    @Override
    public JSONPrimitive getAsJsonPrimitive(String memberName) {
        return null;
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
        if (isJsonObject()){
            return  this;
        }
        throw new IllegalStateException("This is not JSONObject");
    }

    @Override
    public JSONPrimitive getAsJsonPrimitive() {
        if (isJsonPrimitive()) {
            return (JSONPrimitive) this;
        }
        throw new IllegalStateException("This is not JSONPromitive");
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
        return this instanceof JSONObject;
    }

    @Override
    public boolean isJsonPrimitive() {
        return this instanceof JSONPrimitive;
    }

    @Override
    public boolean equals(Object obj){
        return new EqualsBuilder().reflectionEquals(this, obj);
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder().reflectionHashCode(this);
    }
}
